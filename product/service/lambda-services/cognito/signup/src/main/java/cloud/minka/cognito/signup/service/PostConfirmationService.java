package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.command.CognitoSignupEventConverter;
import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.TenantStatus;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PostConfirmationService {
    @Inject
    TenantRepository tenantRepository;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;

    @Inject
    CognitoSignupEventConverter cognitoSignupEventConverter;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    public CognitoSignupEvent process(CognitoSignupEvent input) {
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = userEmail.split("@")[1];

        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);
        GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        TenantStatus tenantStatus = TenantStatus.valueOf(tenant.item().get("status").s());
        return switch (tenantStatus) {
            case PENDING_CONFIGURATION -> finishTenantAdminSetup(input, tenantDomain);
            case ACTIVE -> finishTenantUserSetup(input, tenantDomain);
            default -> throw new IllegalArgumentException("The tenant is not in a valid state");
        };
    }

    public CognitoSignupEvent finishTenantAdminSetup(CognitoSignupEvent input, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::add::group::admin");
        cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.main.admin");
        createTenantGroups(input.userPoolId(), tenantDomain);
        System.out.println("event::cognito::signup::request::tenant::add::group::tenant::admin");
        cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.%s.admins".formatted(tenantDomain));
        setUserCognitoAttributes(input, tenantDomain);
        System.out.println(cognitoSignupEventConverter.responsePostSignup(input));
        return cognitoSignupEventConverter.responsePostSignup(input);
    }

    public CognitoSignupEvent finishTenantUserSetup(CognitoSignupEvent input, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::add::group::tenant::user");
        cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.%s.users".formatted(tenantDomain));
        setUserCognitoAttributes(input, tenantDomain);
        return cognitoSignupEventConverter.responsePostSignup(input);
    }

    public void setUserCognitoAttributes(CognitoSignupEvent input, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::config::cognito::user::attributes");
        cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:domain", tenantDomain);
        cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:tenantId", tenantDomain);
      //  cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:region", input.region());
    }

    public void createTenantGroups(String userPoolId, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::create::group::tenant::admin");
        cognitoTenantRepository.createGroup(userPoolId, "tenant.%s.admins".formatted(tenantDomain));
        System.out.println("event::cognito::signup::request::tenant::create::group::tenant::users");
        cognitoTenantRepository.createGroup(userPoolId, "tenant.%s.users".formatted(tenantDomain));
    }

}
