package cloud.minka.cognito.signup.service;

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
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue="dev-tenants-info-minka-cloud")
    String tableName;

    public CognitoSignupEvent process(CognitoSignupEvent input) {
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = userEmail.split("@")[1];
        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);
        GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        if (tenant.item().get("status").s().equals(TenantStatus.PENDING_CONFIGURATION.name())) {
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
            tenantRepository.updateTenant(tableName, tenantDomain, TenantStatus.ACTIVE);
            return input;
        }


        /*  cognitoClient.adminAddUserToGroup(builder -> builder
                .groupName("user")
                .userPoolId(input.userPoolId())
                .username(input.userName()));*/
        return input;
    }


}
