package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.CognitoSignupEventConverter;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
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
    CognitoSignupEventConverter cognitoSignupEventConverter;

    @Inject
    SnsClient snsClient;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    @ConfigProperty(name = "cloud.minka.tenant.sns.topic", defaultValue = "arn:aws:sns:eu-west-1:631674088803:dev1-tenant-signup-messages-minka-cloud")
    String topicArn;

    public CognitoSignupEvent process(CognitoSignupEvent input) {
        Tenant tenantB = cognitoSignupEventConverter.toTenant(input);
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = userEmail.split("@")[1];
        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);
        GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, tenantDomain);
        TenantStatus tenantStatus = TenantStatus.valueOf(tenant.item().get("status").s());
        return switch (tenantStatus) {
            case PENDING_CONFIGURATION -> finishTenantAdminSetup(input, tenantDomain);
            case ACTIVE -> finishTenantUserSetup(input, tenantDomain, false);
            default -> throw new IllegalArgumentException("The tenant is not in a valid state");
        };
    }

    public CognitoSignupEvent finishTenantAdminSetup(CognitoSignupEvent input, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::add::group::admin");
        cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.main.admin");
        createTenantGroups(input.userPoolId(), tenantDomain);
        return finishTenantUserSetup(input, tenantDomain, true);
    }

    public CognitoSignupEvent finishTenantUserSetup(CognitoSignupEvent input, String tenantDomain, boolean isTenantAdmin) {
        System.out.println("event::cognito::signup::request::tenant::add::group::tenant::user");
        cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.%s.users".formatted(tenantDomain));
        setUserCognitoAttributes(input, tenantDomain);
        sendSNSMessage(input, isTenantAdmin);
        return cognitoSignupEventConverter.responsePostSignup(input);
    }

    public void setUserCognitoAttributes(CognitoSignupEvent input, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::config::cognito::user::attributes");
        cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:domain", tenantDomain);
        cognitoTenantRepository.adminUpdateUserAttributes(input.userPoolId(), input.userName(), "custom:tenantId", tenantDomain);
    }

    public void createTenantGroups(String userPoolId, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::create::group::tenant::users");
        cognitoTenantRepository.createGroup(userPoolId, "tenant.%s.users".formatted(tenantDomain));
    }

    private void sendSNSMessage(CognitoSignupEvent input, boolean isTenantAdmin) {
        System.out.println("event::cognito::signup::request::tenant::send::sns::message");
        String email = input.request().get("userAttributes").get("email").asText();
        String tenantDomain = email.split("@")[1];
        String userName = input.userName();
        snsClient
                .publish(builder -> builder.topicArn(topicArn)
                        .message("New user signup for tenant %s".formatted(input.request().get("userAttributes").get("email").asText()))
                        .subject("NEW_USER_SIGNUP")
                        .messageAttributes(new HashMap<>() {{
                            put("SNS_MESSAGE_TYPE", MessageAttributeValue.builder()
                                    .dataType("String").stringValue("NEW_USER_SIGNUP").build());
                            put("tenantId", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(tenantDomain)
                                    .build());
                            put("userEmail", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(email)
                                    .build());
                            put("userName", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(userName)
                                    .build());
                            put("isTenantAdmin", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(isTenantAdmin ? "true" : "false")
                                    .build());
                            put("tenantDomain", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(tenantDomain)
                                    .build());
                        }})
                        .build());
    }

}
