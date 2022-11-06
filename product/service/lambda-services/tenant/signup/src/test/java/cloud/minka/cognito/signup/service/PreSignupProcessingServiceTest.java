package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.TenantConverter;
import cloud.minka.cognito.signup.exception.DomainException;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CallerContext;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.cognito.TriggerSource;
import cloud.minka.service.model.tenant.TenantCreate;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import javax.inject.Inject;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
class PreSignupProcessingServiceTest {
    final String response = "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}";


    @Inject
    PreSignupProcessingService preSignupProcessingService;
    @InjectMock
    CognitoTenantRepository cognitoTenantRepository;
    @InjectMock
    TenantRepository tenantRepository;

    @InjectMock
    TenantConverter tenantConverter;
    @Inject
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

    }

    CognitoSignupEvent cognitoSignupEvent(String request, String response) {
        try {
            return new CognitoSignupEvent(
                    "1",
                    "eu-west-1",
                    "dummy",
                    "google_102051572441221876770",
                    new CallerContext(
                            "dummy",
                            "dummy"
                    ),
                    TriggerSource.PreSignUp_SignUp,
                    objectMapper.readTree(request),
                    objectMapper.readTree(response)

            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    TenantCreate tenantCreate(String PK, String SK, String userEmail, TenantStatus tenantStatus) {
        return new TenantCreate(
                PK,
                SK,
                userEmail,
                tenantStatus,
                TenantType.HOSTED,
                "dummy");
    }

    @Test
    void testCantCreateTenantWithFreeDomain() {
        String request = "{\"userAttributes\": {\"email\": \"test@gmail.com\"}}";
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }

    @Test
    void testCantCreateTenantWithLocalhost() {
        String request = "{\"userAttributes\": {\"email\": \"test@LocAlHosT.com\"}}";
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }

    @Test
    void testCantCreateTenantWithInValidSubDomain() {
        String request = "{\"userAttributes\": {\"email\": \"test@test.mindera.co.uk\"}}";
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }

    @Test
    void testCantCreateTenantWithInValidSubDomainMoreThanTwoLetterss() {
        String request = "{\"userAttributes\": {\"email\": \"test@tmindera.com.uk\"}}";
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }

    @Test
    void testCantCreateTenantWithEmptyEmail() {
        String request = "{\"userAttributes\": {\"email\": \"\"}}";
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }

    @Test
    void testCantCreateTenantWithBadEmail() {
        String request = "{\"userAttributes\": {\"email\": \"diogo.com\"}}";
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }


    @Test
    void testCantCreateTenantUserIfEmailExists() {
        String request = "{\"userAttributes\": {\"email\": \"test@mindera.co.uk\"}}";

        when(cognitoTenantRepository.emailExists("test@mindera.co.uk", "dummy"))
                .thenReturn(true);

        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }

    @Test
    void testCantCreateTenantUserIfTenantIsPending() {
        String request = "{\"userAttributes\": {\"email\": \"test@mindera.co.uk\"}}";
        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#mindera.co.uk").build());
                    put("SK", AttributeValue.builder().s("T#mindera.co.uk").build());
                    put("status", AttributeValue.builder().s("PENDING_CONFIGURATION").build());
                }})
                .build();
        when(cognitoTenantRepository.emailExists("test@mindera.co.uk", "dummy"))
                .thenReturn(false);
        when(tenantRepository
                .getTenantFromTable("dev-tenants-info-minka-cloud", "mindera.co.uk"))
                .thenReturn(getItemResponse);
        when(tenantConverter.response(cognitoSignupEvent(request, response)))
                .thenReturn(cognitoSignupEvent(request, response));
        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse)).thenReturn(
                tenantCreate("T#mindera.co.uk",
                        "T#mindera.co.uk",
                        "test@mindera.co.uk",
                        TenantStatus.PENDING_CONFIGURATION)
        );
        assertThrows(DomainException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        });
    }


    @Test
    void testCantCreateTenantUserIfStatusIsUnexpected() {
        String request = "{\"userAttributes\": {\"email\": \"test@mindera.co.uk\"}}";

        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#mindera.co.uk").build());
                    put("SK", AttributeValue.builder().s("T#mindera.co.uk").build());
                    put("status", AttributeValue.builder().s("UNKNOWN").build());
                }})
                .build();

        when(cognitoTenantRepository.emailExists("test@mindera.co.uk", "dummy"))
                .thenReturn(false);
        when(tenantRepository
                .getTenantFromTable("dev-tenants-info-minka-cloud", "mindera.co.uk"))
                .thenReturn(getItemResponse);
        when(tenantConverter.response(cognitoSignupEvent(request, response)))
                .thenReturn(cognitoSignupEvent(request, response));
        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse)).thenReturn(
                tenantCreate("T#mindera.co.uk",
                        "T#mindera.co.uk",
                        "test@mindera.co.uk",
                        TenantStatus.UNKNOWN)
        );
        String message = assertThrows(IllegalArgumentException.class, () -> {
            preSignupProcessingService.process(cognitoSignupEvent(request, response));
        }).getMessage();
        assertEquals("The tenant is not in a valid state", message);
    }

    @Test
    void testCanCreateTenantWithValidSubDomain() {
        String request = "{\"userAttributes\": {\"email\": \"test@mindera.co.uk\"}}";

        when(cognitoTenantRepository.emailExists("test@mindera.co.uk", "dummy"))
                .thenReturn(false);
        when(tenantRepository
                .getTenantFromTable("dev-tenants-info-minka-cloud", "mindera.co.uk"))
                .thenReturn(GetItemResponse.builder().item(new HashMap<>()).build());
        when(tenantConverter.response(cognitoSignupEvent(request, response)))
                .thenReturn(cognitoSignupEvent(request, response));
        String pk = "T#mindera.co.uk";
        String sk = "T#mindera.co.uk";
        PutItemRequest put = tenantConverter.convertTenantToPutItemRequest("dev-tenants-info-minka-cloud",
                tenantCreate(pk, sk, "test@mindera.co.uk", TenantStatus.PENDING_CONFIGURATION));
        doNothing().when(tenantRepository).insertTenantIntoTable(put);
        CognitoSignupEvent cognitoSignupEvent = preSignupProcessingService.process(cognitoSignupEvent(request, response));
        assert cognitoSignupEvent.equals(cognitoSignupEvent(request, response));
    }

    @Test
    void testCanCreateTenantUserIfTenantActive() {
        String request = "{\"userAttributes\": {\"email\": \"test@mindera.co.uk\"}}";

        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#mindera.co.uk").build());
                    put("SK", AttributeValue.builder().s("T#mindera.co.uk").build());
                    put("status", AttributeValue.builder().s("ACTIVE").build());
                }})
                .build();

        when(cognitoTenantRepository.emailExists("test@mindera.co.uk", "dummy"))
                .thenReturn(false);
        when(tenantRepository
                .getTenantFromTable("dev-tenants-info-minka-cloud", "mindera.co.uk"))
                .thenReturn(getItemResponse);
        when(tenantConverter.response(cognitoSignupEvent(request, response)))
                .thenReturn(cognitoSignupEvent(request, response));
        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse)).thenReturn(
                tenantCreate("T#mindera.co.uk",
                        "T#mindera.co.uk",
                        "test@mindera.co.uk",
                        TenantStatus.ACTIVE)
        );

        CognitoSignupEvent cognitoSignupEvent = preSignupProcessingService.process(cognitoSignupEvent(request, response));
        assert cognitoSignupEvent.equals(cognitoSignupEvent(request, response));
    }

}