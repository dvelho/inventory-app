package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.command.CognitoSignupEventConverter;
import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.TenantStatus;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@ApplicationScoped
public final class PreSignupProcessingService {


    @Inject
    TenantRepository tenantRepository;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;
    @Inject
    CognitoSignupEventConverter cognitoSignupEventConverter;
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

        tenantRepository.createTenantTable(tableName);
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = userEmail.split("@")[1];

        if (isFreeDomainProvider(tenantDomain)) {
            throw new IllegalArgumentException("Free Domains are not allowed");
        }

        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);

        // Check if the tenant exists
        GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        System.out.println("event::cognito::signup::request::tenant::response:" + tenant);
        CognitoSignupEvent responseSuccess = cognitoSignupEventConverter.response(input);
        System.out.println("event::cognito::signup::request::tenant::response::success:" + responseSuccess);
        if (tenant.item().size() == 0) {
            System.out.printf("event::cognito::signup::request::tenant::create::table::tenant::%s%n", tenantDomain);
            tenantRepository.insertTenantIntoTable(tableName, tenantDomain, userEmail);
            return responseSuccess;
        }
        //Check if the tenant is in pending configuration
        System.out.println("event::cognito::signup::request::tenant::exists");
        TenantStatus tenantStatus = TenantStatus.valueOf(tenant.item().get("status").s());
        return switch (tenantStatus) {
            case PENDING_CONFIGURATION ->
                    throw new IllegalArgumentException("Your domain exists but is not yet fully configured. Please contact the person responsible for your Organization.");
            case ACTIVE -> responseSuccess;
            default -> throw new IllegalArgumentException("The tenant is not in a valid state");
        };
    }

    private boolean isFreeDomainProvider(String tenantDomain) {
        String resourcePath = "/free-email-providers.json";
        System.out.println("event::cognito::signup::request::tenant::domain::free::provider::check");
        try {
            InputStream ins = PreSignupProcessingService.class.getResourceAsStream(resourcePath);
            if (ins == null) {
                System.out.println("module came empty, now trying to load from GreetingResource");
                return false;
            }
            List list = objectMapper.readValue(ins, List.class);
            return list.contains(tenantDomain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}


