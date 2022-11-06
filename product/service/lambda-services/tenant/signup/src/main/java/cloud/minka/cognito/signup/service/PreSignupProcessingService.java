package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.TenantConverter;
import cloud.minka.cognito.signup.exception.DomainException;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.entity.EntityType;
import cloud.minka.service.model.tenant.TenantCreate;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.soabase.recordbuilder.core.RecordBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@ApplicationScoped
@RecordBuilder.Include({TenantCreate.class})// generates a record builder for ImportedRecord
public final class PreSignupProcessingService {
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    TenantRepository tenantRepository;

    CognitoTenantRepository cognitoTenantRepository;
    TenantConverter tenantConverter;

    public PreSignupProcessingService(TenantRepository tenantRepository, CognitoTenantRepository cognitoTenantRepository, TenantConverter tenantConverter) {
        this.tenantRepository = tenantRepository;
        this.cognitoTenantRepository = cognitoTenantRepository;
        this.tenantConverter = tenantConverter;
    }

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

        if (isEmailInvalid(userEmail)) {
            throw new DomainException("User email is invalid");
        }

        String tenantDomain = userEmail.split("@")[1];
        String PK = EntityType.TENANT.prefixPK() + tenantDomain;
        String SK = EntityType.TENANT.prefixSK() + tenantDomain;

        if (isFreeDomainProvider(tenantDomain)) {
            throw new DomainException("Free Domains are not allowed");
        }


        if (cognitoTenantRepository.emailExists(userEmail, input.userPoolId())) {
            System.out.println("event::cognito::signup::request::tenantCreate::email::exists:" + userEmail);
            throw new DomainException("Email already exists. Maybe you already have an account?");
        }

        System.out.println("event::cognito::signup::request::tenantCreate::domain:" + tenantDomain);

        // Check if the tenantCreate exists
        GetItemResponse tenantDb = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        CognitoSignupEvent responseSuccess = tenantConverter.response(input);
        TenantCreate tenantCreate;
        if (tenantDb.item().size() == 0) { // tenantCreate does not exist
            tenantCreate = new TenantCreate(
                    PK,
                    SK,
                    userEmail,
                    TenantStatus.PENDING_CONFIGURATION,
                    TenantType.HOSTED,
                    input.userPoolId());
            System.out.printf("event::cognito::signup::request::tenantCreate::create::tenantCreate::%s", tenantDomain);
            tenantRepository.insertTenantIntoTable(tenantConverter.convertTenantToPutItemRequest(tableName, tenantCreate));
            System.out.println("event::cognito::signup::request::tenantCreate::response::success:" + responseSuccess);
            return responseSuccess;
        }

        tenantCreate = tenantConverter.convertGetItemResponseToTenant(tenantDb);
        //Check if the tenantCreate is in pending configuration
        return switch (tenantCreate.status()) {
            case PENDING_CONFIGURATION ->
                    throw new DomainException("Your domain exists but is not yet fully configured. Please contact the person responsible for your Organization.");
            case ACTIVE -> {
                System.out.println("event::cognito::signup::request::tenantCreate::response::success:" + responseSuccess);
                yield responseSuccess;
            }
            default -> throw new IllegalArgumentException("The tenant is not in a valid state");
        };
    }

    private boolean isEmailInvalid(String userEmail) {
        //This regex validates mindera.com, mindera.co.uk but does not validate sub.mindera.uk

        String regexPattern = "^[\\w!#$%&'*/=?`{|}~^-]+(?:\\.[\\w!#$%&'*/=?`{|}~^-]+)*" +
                "((?:@(?:[a-zA-Z0-9-]+\\.){1}[a-zA-Z]{2,6}$)" +
                "|(?:@(?:[a-zA-Z0-9-]+\\.){1}(?:[a-zA-Z0-9-]{2}\\.){1}[a-zA-Z]{2}$))";
        return userEmail == null || userEmail.isEmpty() || !userEmail.matches(regexPattern);
    }


    private boolean isFreeDomainProvider(String tenantDomain) {
        if (tenantDomain.toLowerCase().contains("localhost")) {
            return true;
        }
        String resourcePath = "/free-email-providers.json";
        System.out.println("event::cognito::signup::request::tenant::domain::free::provider::check");
        try {
            InputStream ins = PreSignupProcessingService.class.getResourceAsStream(resourcePath);
            return new ObjectMapper().readValue(ins, List.class).contains(tenantDomain.toLowerCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}


