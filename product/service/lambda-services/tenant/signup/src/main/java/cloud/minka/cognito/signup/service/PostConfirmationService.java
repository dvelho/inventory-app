package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.Converter;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.tenant.Tenant;
import cloud.minka.service.model.tenant.TenantStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;


@ApplicationScoped
public class PostConfirmationService {
    @Inject
    TenantRepository tenantRepository;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;

    @Inject
    Converter converter;

    @Inject
    SnsClient snsClient;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    @ConfigProperty(name = "cloud.minka.tenant.sns.topic", defaultValue = "arn:aws:sns:eu-west-1:631674088803:dev1-tenant-signup-messages-minka-cloud")
    String topicArn;

    public CognitoSignupEvent process(CognitoSignupEvent input) {
        String tenantDomain = input.request().get("userAttributes").get("email").asText().split("@")[1];
        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);
        GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        if (!tenant.hasItem()) {
            throw new IllegalArgumentException("Tenant not found");
        }
        Tenant tenantModel = converter.convertGetItemResponseToTenant(tenant);
        boolean isTenantAdmin = tenantModel.status().equals(TenantStatus.PENDING_CONFIGURATION);
        SignupUser signupUser = converter.convertCognitoSignupEventToSignupUser(input, isTenantAdmin);
        sendSNSMessage(tenantModel, signupUser);
        switch (tenantModel.status()) {
            case PENDING_CONFIGURATION -> finishTenantAdminSetup(tenantModel, signupUser);
            case ACTIVE -> finishTenantUserSetup(tenantModel, signupUser);
            default -> throw new IllegalArgumentException("The tenant is not in a valid state");
        }
        ;
        sendSNSMessage(tenantModel, signupUser);
        return converter.responsePostSignup(input);
    }

    public void finishTenantAdminSetup(Tenant tenant, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenant::add::group::admin");
        cognitoTenantRepository.adminAddUserToGroup(tenant.userPoolId(), signupUser.userName(), "tenant.main.admin");
        createTenantGroups(tenant.userPoolId(), tenant.PK());
        finishTenantUserSetup(tenant, signupUser);
    }

    public void finishTenantUserSetup(Tenant tenant, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenant::add::group::tenant::user");
        cognitoTenantRepository.adminAddUserToGroup(tenant.userPoolId(), signupUser.userName(), "tenant.%s.users".formatted(tenant.PK()));
        setUserCognitoAttributes(tenant, signupUser);

    }

    public void setUserCognitoAttributes(Tenant tenant, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenant::config::cognito::user::attributes");
        cognitoTenantRepository.adminUpdateUserAttributes(tenant.userPoolId(), signupUser.userName(), "custom:domain", tenant.PK());
        cognitoTenantRepository.adminUpdateUserAttributes(tenant.userPoolId(), signupUser.userName(), "custom:tenantId", tenant.PK());
    }

    public void createTenantGroups(String userPoolId, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::create::group::tenant::users");
        cognitoTenantRepository.createGroup(userPoolId, "tenant.%s.users".formatted(tenantDomain));
    }

    private void sendSNSMessage(Tenant tenant, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenant::send::sns::message");
        String snsMessage = converter.convertTenantAndSignupUserToSNSMessage(tenant, signupUser);
        snsClient
                .publish(builder -> builder.topicArn(topicArn)
                        .message(snsMessage)
                        .subject("NEW_USER_SIGNUP")
                        .messageAttributes(new HashMap<>() {{
                            put("MESSAGE_TYPE", MessageAttributeValue.builder()
                                    .dataType("String").stringValue("NEW_USER_SIGNUP").build());
                            put("tenantDomain", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(tenant.PK())
                                    .build());
                            put("isTenantAdmin", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(String.valueOf(signupUser.isTenantAdmin()))
                                    .build());
                        }})
                        .build());
    }

}
