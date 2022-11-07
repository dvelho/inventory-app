package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.TenantConverter;
import cloud.minka.cognito.signup.exception.TenantNotFoundException;
import cloud.minka.cognito.signup.exception.TentantStatusInvalidException;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CallerContext;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.cognito.TriggerSource;
import cloud.minka.service.model.tenant.TenantCreate;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@QuarkusTest
class PostConfirmationServiceTest {

    @Inject
    PostConfirmationService postConfirmationService;

    @InjectMock
    TenantRepository tenantRepository;

    @InjectMock
    CognitoTenantRepository cognitoTenantRepository;

    @InjectMock
    SnsClient snsClient;

    @InjectMock
    TenantConverter tenantConverter;

    @Inject
    ObjectMapper objectMapper;

    CognitoSignupEvent cognitoSignupEvent(String request, String response) {
        try {
            return new CognitoSignupEvent(
                    "1",
                    "eu-west-1",
                    "dummy",
                    "dummy",
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

    TenantCreate tenantCreate(String PK, String SK, String adminEmail, TenantStatus tenantStatus) {
        return new TenantCreate(
                PK,
                SK,
                adminEmail,
                tenantStatus,
                TenantType.HOSTED,
                "dummy");
    }

    @Test
    void testIfNoTenantThrowsTenantNotFoundExceptionAndDeletesUserAndGroup() {
        CognitoSignupEvent input = cognitoSignupEvent(
                "{\"userAttributes\": {\"email\": \"test@minka.cloud\"}}",
                "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}"
        );

        when(tenantRepository.getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud"))
                .thenReturn(GetItemResponse.builder()
                        .item(Map.of())
                        .build());
        assertThrows(TenantNotFoundException.class, () -> postConfirmationService.process(input));

        verify(tenantRepository, Mockito.times(1))
                .getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(1))
                .deleteUser("dummy");
        verify(cognitoTenantRepository, Mockito.times(1))
                .deleteGroup("tenant.%s.users".formatted("T#minka.cloud"));
    }

    @Test
    void testIfNotAdminThrowsTenantStatusExceptionAndDeletesUserAndNotGroupWhenPending() {
        CognitoSignupEvent input = cognitoSignupEvent(
                "{\"userAttributes\": {\"email\": \"test@minka.cloud\"}}",
                "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}"
        );
        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("SK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("status", AttributeValue.builder().s("PENDING_CONFIGURATION").build());
                    put("adminEmail", AttributeValue.builder().s("test11@minka.cloud").build());
                }})
                .build();
        when(tenantRepository.getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud"))
                .thenReturn(getItemResponse);

        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse))
                .thenReturn(
                        tenantCreate("T#minka.cloud",
                                "T#minka.cloud",
                                "test11@minka.cloud",
                                TenantStatus.PENDING_CONFIGURATION)
                );
        assertThrows(TentantStatusInvalidException.class, () -> postConfirmationService.process(input));

        verify(tenantRepository, Mockito.times(1))
                .getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(1))
                .deleteUser("dummy");
        verify(cognitoTenantRepository, Mockito.times(0))
                .deleteGroup("tenant.%s.users".formatted("T#minka.cloud"));
    }


    @Test
    void testThrowsTenantStatusExceptionAndDeletesUserAndGroupWhenUnknownAndUserIsAdmin() {
        CognitoSignupEvent input = cognitoSignupEvent(
                "{\"userAttributes\": {\"email\": \"test@minka.cloud\"}}",
                "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}"
        );
        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("SK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("status", AttributeValue.builder().s("UNKNOWN").build());
                    put("adminEmail", AttributeValue.builder().s("test@minka.cloud").build());
                }})
                .build();
        when(tenantRepository.getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud"))
                .thenReturn(getItemResponse);

        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse))
                .thenReturn(
                        tenantCreate("T#minka.cloud",
                                "T#minka.cloud",
                                "test@minka.cloud",
                                TenantStatus.UNKNOWN)
                );

        when(tenantConverter.convertCognitoSignupEventToSignupUser(any(CognitoSignupEvent.class), eq(false))).thenReturn(
                new SignupUser("dummy", "test@minka.cloud", true)
        );

        assertThrows(TentantStatusInvalidException.class, () -> postConfirmationService.process(input));

        verify(tenantRepository, Mockito.times(1))
                .getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(1))
                .deleteUser("dummy");
        verify(cognitoTenantRepository, Mockito.times(1))
                .deleteGroup("tenant.%s.users".formatted("T#minka.cloud"));
    }

    @Test
    void testCreateUserAndGroupWhenUserIsAdminAndPendingConfiguration() {
        CognitoSignupEvent input = cognitoSignupEvent(
                "{\"userAttributes\": {\"email\": \"test@minka.cloud\"}}",
                "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}"
        );
        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("SK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("status", AttributeValue.builder().s("PENDING_CONFIGURATION").build());
                    put("adminEmail", AttributeValue.builder().s("test@minka.cloud").build());
                }})
                .build();

        PublishRequest publishRequest = PublishRequest.builder().topicArn("dummy").message("dummy").build();
        when(tenantRepository.getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud"))
                .thenReturn(getItemResponse);

        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse))
                .thenReturn(
                        tenantCreate("T#minka.cloud",
                                "T#minka.cloud",
                                "test@minka.cloud",
                                TenantStatus.PENDING_CONFIGURATION)
                );

        when(tenantConverter.convertCognitoSignupEventToSignupUser(any(CognitoSignupEvent.class), eq(true))).thenReturn(
                new SignupUser("dummy", "test@minka.cloud", true)
        );

        doNothing().when(cognitoTenantRepository).adminAddUserToGroup("dummy", "dummy", "tenant.main.admin");
        doNothing().when(cognitoTenantRepository).createGroup("dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        doNothing().when(cognitoTenantRepository).adminAddUserToGroup("dummy", "dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        doNothing().when(cognitoTenantRepository).adminUpdateUserAttributes("dummy", "dummy", "custom:domain", "T#minka.cloud");
        doNothing().when(cognitoTenantRepository).adminUpdateUserAttributes("dummy", "dummy", "custom:tenantId", "T#minka.cloud");

        when(tenantConverter.convertTenantAndSignupUserToSNSMessage(any(TenantCreate.class), any(SignupUser.class))).thenReturn("dummy");
        when(tenantConverter.convertTenantAndSignupUserToSNSRequest(anyString(), any(TenantCreate.class), any(SignupUser.class))).thenReturn(
                publishRequest
        );
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(PublishResponse.builder().build());

        PublishResponse publishResponse = PublishResponse.builder().messageId("dummy").build();
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(publishResponse);
        when(tenantConverter.responsePostSignup(input)).thenReturn(input);
        CognitoSignupEvent output = postConfirmationService.process(input);

        verify(tenantRepository, Mockito.times(1))
                .getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(1)).adminAddUserToGroup("dummy", "dummy", "tenant.main.admin");
        verify(cognitoTenantRepository, Mockito.times(1)).createGroup("dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        verify(cognitoTenantRepository, Mockito.times(1)).adminAddUserToGroup("dummy", "dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        verify(cognitoTenantRepository, Mockito.times(1)).adminUpdateUserAttributes("dummy", "dummy", "custom:domain", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(1)).adminUpdateUserAttributes("dummy", "dummy", "custom:tenantId", "T#minka.cloud");
        verify(snsClient, Mockito.times(1)).publish(publishRequest);
        assert output.equals(input);
    }


    @Test
    void testCreateUserWhenUserIsNotAdminAndActiveDoesNotAddToAdminGroup() {
        CognitoSignupEvent input = cognitoSignupEvent(
                "{\"userAttributes\": {\"email\": \"test@minka.cloud\"}}",
                "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}"
        );
        GetItemResponse getItemResponse = GetItemResponse.builder()
                .item(new HashMap<>() {{
                    put("PK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("SK", AttributeValue.builder().s("T#minka.cloud").build());
                    put("status", AttributeValue.builder().s("ACTIVE").build());
                    put("adminEmail", AttributeValue.builder().s("test@minka.cloud").build());
                }})
                .build();

        PublishRequest publishRequest = PublishRequest.builder().topicArn("dummy").message("dummy").build();
        when(tenantRepository.getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud"))
                .thenReturn(getItemResponse);

        when(tenantConverter.convertGetItemResponseToTenant(getItemResponse))
                .thenReturn(
                        tenantCreate("T#minka.cloud",
                                "T#minka.cloud",
                                "test@minka.cloud",
                                TenantStatus.ACTIVE)
                );

        when(tenantConverter.convertCognitoSignupEventToSignupUser(any(CognitoSignupEvent.class), eq(false))).thenReturn(
                new SignupUser("dummy", "test@minka.cloud", false)
        );

        doNothing().when(cognitoTenantRepository).adminAddUserToGroup("dummy", "dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        doNothing().when(cognitoTenantRepository).adminUpdateUserAttributes("dummy", "dummy", "custom:domain", "T#minka.cloud");
        doNothing().when(cognitoTenantRepository).adminUpdateUserAttributes("dummy", "dummy", "custom:tenantId", "T#minka.cloud");

        when(tenantConverter.convertTenantAndSignupUserToSNSMessage(any(TenantCreate.class), any(SignupUser.class))).thenReturn("dummy");
        when(tenantConverter.convertTenantAndSignupUserToSNSRequest(anyString(), any(TenantCreate.class), any(SignupUser.class))).thenReturn(
                publishRequest
        );

        when(snsClient.publish(any(PublishRequest.class))).thenReturn(PublishResponse.builder().build());

        PublishResponse publishResponse = PublishResponse.builder().messageId("dummy").build();
        when(snsClient.publish(any(PublishRequest.class))).thenReturn(publishResponse);
        when(tenantConverter.responsePostSignup(input)).thenReturn(input);
        CognitoSignupEvent output = postConfirmationService.process(input);

        verify(tenantRepository, Mockito.times(1))
                .getTenantFromTable("dev-tenants-info-minka-cloud", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(0)).adminAddUserToGroup("dummy", "dummy", "tenant.main.admin");
        verify(cognitoTenantRepository, Mockito.times(0)).createGroup("dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        verify(cognitoTenantRepository, Mockito.times(1)).adminAddUserToGroup("dummy", "dummy", "tenant.%s.users".formatted("T#minka.cloud"));
        verify(cognitoTenantRepository, Mockito.times(1)).adminUpdateUserAttributes("dummy", "dummy", "custom:domain", "T#minka.cloud");
        verify(cognitoTenantRepository, Mockito.times(1)).adminUpdateUserAttributes("dummy", "dummy", "custom:tenantId", "T#minka.cloud");
        verify(snsClient, Mockito.times(1)).publish(publishRequest);
        assert output.equals(input);
    }


}


