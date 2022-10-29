package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.ResponseSignup;
import cloud.minka.cognito.signup.model.cloudformation.TenantStatus;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public final class PreSignupProcessingService {


    @Inject
    TenantRepository tenantRepository;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue="dev-tenants-info-minka-cloud")
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

      //  tenantRepository.createTenantTable(tableName);
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = userEmail.split("@")[1];
        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);

        // Check if the tenant exists
        GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        System.out.println("event::cognito::signup::request::tenant::response:" + tenant);
        CognitoSignupEvent responseSuccess = createSignupEvent(input);
        System.out.println("event::cognito::signup::request::tenant::response::success:" + responseSuccess);
        if (tenant.item().size() == 0) {
            System.out.println("event::cognito::signup::request::tenant::add::group::admin");
            cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.main.admin");
            System.out.println("event::cognito::signup::request::tenant::create::group::tenant::admin");
            cognitoTenantRepository.createGroup(input.userPoolId(), "tenant.%s.admins".formatted(tenantDomain));
            System.out.println("event::cognito::signup::request::tenant::create::group::tenant::users");
            cognitoTenantRepository.createGroup(input.userPoolId(), "tenant.%s.users".formatted(tenantDomain));
            System.out.println("event::cognito::signup::request::tenant::add::group::tenant::admin");
            cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.%s.admins".formatted(tenantDomain));
            cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:domain", tenantDomain);
            cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:tenantId", tenantDomain);
            cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:region", input.region());
            System.out.println("event::cognito::signup::request::tenant::create::table::tenant::%s".formatted(tenantDomain));
            tenantRepository.insertTenantIntoTable(tableName, tenantDomain);
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

    public CognitoSignupEvent createSignupEvent(CognitoSignupEvent input) {
        return new CognitoSignupEvent(
                input.version(),
                input.region(),
                input.userPoolId(),
                input.userName(),
                input.callerContext(),
                input.triggerSource(),
                input.request(),
                new ResponseSignup("true", "false", "false")
        );
    }


}


