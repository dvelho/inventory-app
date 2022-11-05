package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.Converter;
import cloud.minka.cognito.signup.converter.TenantBuilder;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.tenant.TenantCreate;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.soabase.recordbuilder.core.RecordBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@ApplicationScoped
@RecordBuilder.Include({TenantCreate.class})// generates a record builder for ImportedRecord
public final class PreSignupProcessingService {


    @Inject
    TenantRepository tenantRepository;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;
    @Inject
    Converter converter;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    /**
     * Process pre signup response signup.
     * If the tenant does not exist, then we will create it.
     * If the tenant does exist, but the user is not the admin, then we will return an error.
     *
     * @param input the input
     * @return the response signup
     */
    public CognitoSignupEvent process(CognitoSignupEvent input) {

        //tenantRepository.createTenantTable(tableName);
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = userEmail.split("@")[1];

        if (isFreeDomainProvider(tenantDomain)) {
            throw new IllegalArgumentException("Free Domains are not allowed");
        }

        if (cognitoTenantRepository.emailExists(userEmail, input.userPoolId())) {
            System.out.println("event::cognito::signup::request::tenantCreate::email::exists:" + userEmail);
            throw new IllegalArgumentException("Email already exists. Maybe you already have an account?");
        }

        System.out.println("event::cognito::signup::request::tenantCreate::domain:" + tenantDomain);

        // Check if the tenantCreate exists
        GetItemResponse tenantDb = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        CognitoSignupEvent responseSuccess = converter.response(input);
        TenantCreate tenantCreate;
        if (tenantDb.item().size() == 0) { // tenantCreate does not exist
            tenantCreate = TenantBuilder.builder()
                    .PK(tenantDomain)
                    .SK(tenantDomain)
                    .adminEmail(userEmail)
                    .status(TenantStatus.PENDING_CONFIGURATION)
                    .type(TenantType.HOSTED)
                    .userPoolId(input.userPoolId())
                    .build();

            System.out.printf("event::cognito::signup::request::tenantCreate::create::tenantCreate::%s", tenantDomain);
            tenantRepository.insertTenantIntoTable(converter.convertTenantToPutItemRequest(tableName, tenantCreate));
            System.out.println("event::cognito::signup::request::tenantCreate::response::success:" + responseSuccess);
            return responseSuccess;
        }

        tenantCreate = converter.convertGetItemResponseToTenant(tenantDb);
        //Check if the tenantCreate is in pending configuration
        return switch (tenantCreate.status()) {
            case PENDING_CONFIGURATION ->
                    throw new IllegalArgumentException("Your domain exists but is not yet fully configured. Please contact the person responsible for your Organization.");
            case ACTIVE -> {
                System.out.println("event::cognito::signup::request::tenantCreate::response::success:" + responseSuccess);
                yield responseSuccess;
            }
            default -> throw new IllegalArgumentException("The tenantCreate is not in a valid state");
        };
    }

    private boolean isFreeDomainProvider(String tenantDomain) {
        String resourcePath = "/free-email-providers.json";
        System.out.println("event::cognito::signup::request::tenant::domain::free::provider::check");
        try {
            InputStream ins = PreSignupProcessingService.class.getResourceAsStream(resourcePath);
            return objectMapper.readValue(ins, List.class).contains(tenantDomain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}


