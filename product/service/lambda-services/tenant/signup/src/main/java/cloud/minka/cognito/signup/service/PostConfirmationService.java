package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.converter.TenantConverter;
import cloud.minka.cognito.signup.exception.TenantNotFoundException;
import cloud.minka.cognito.signup.exception.TentantStatusInvalidException;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.entity.EntityType;
import cloud.minka.service.model.tenant.TenantCreate;
import cloud.minka.service.model.tenant.TenantStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class PostConfirmationService {
    @Inject
    TenantRepository tenantRepository;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;

    @Inject
    TenantConverter tenantConverter;

    @Inject
    SnsClient snsClient;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    @ConfigProperty(name = "cloud.minka.tenant.sns.topic", defaultValue = "arn:aws:sns:eu-west-1:631674088803:dev1-tenant-signup-messages-minka-cloud")
    String topicArn;

    public CognitoSignupEvent process(CognitoSignupEvent input) {
        SignupUser signupUser = null;
        String userName = input.userName();
        String userEmail = input.request().get("userAttributes").get("email").asText();
        String PK = EntityType.TENANT.prefixPK() + userEmail.split("@")[1];
        try {
            System.out.println("event::cognito::signup::request::tenant::domain:" + PK);
            GetItemResponse tenant = tenantRepository.getTenantFromTable(tableName, PK);
            if (!tenant.hasItem() || tenant.item().isEmpty()) {
                throw new TenantNotFoundException("Tenant not found");
            }
            TenantCreate tenantCreateModel = tenantConverter.convertGetItemResponseToTenant(tenant);
            boolean isTenantPendingConfig = tenantCreateModel.status().equals(TenantStatus.PENDING_CONFIGURATION);

            if (isTenantPendingConfig && !userEmail.equalsIgnoreCase(tenantCreateModel.adminEmail())) {
                throw new TentantStatusInvalidException("Your email is not the admin email and the tenant is not yet configured");
            }

            signupUser = tenantConverter.convertCognitoSignupEventToSignupUser(input, isTenantPendingConfig);
            switch (tenantCreateModel.status()) {
                case PENDING_CONFIGURATION -> finishTenantAdminSetup(tenantCreateModel, signupUser);
                case ACTIVE -> finishTenantUserSetup(tenantCreateModel, signupUser);
                default -> throw new TentantStatusInvalidException("The tenant is not in a valid state");
            }

            sendSNSMessage(tenantCreateModel, signupUser);
            return tenantConverter.responsePostSignup(input);
        } catch (TenantNotFoundException e) {
            System.out.println("event::cognito::signup::request::error:" + e.getMessage());
            deleteCognitoUser(userName);
            deleteGroup(PK);
            throw e;
        } catch (TentantStatusInvalidException e) {
            System.out.println("event::cognito::signup::request::error:" + e.getMessage());
            deleteCognitoUser(userName);
            if (signupUser != null && signupUser.isTenantAdmin()) {
                deleteGroup(PK);
            }
            throw e;
        } catch (Exception e) {
            System.out.println("event::cognito::signup::request::error:" + e.getMessage());
            deleteCognitoUser(userName);
            if (signupUser != null && signupUser.isTenantAdmin()) {
                deleteGroup(PK);
            }
            throw e;
        }
    }

    private void deleteCognitoUser(String username) {
        System.out.println("event::cognito::signup::request::delete::user:" + username);
        cognitoTenantRepository.deleteUser(username);
    }

    private void deleteGroup(String tenantDomain) {
        System.out.println("event::cognito::signup::request::delete::group:" + "tenant.%s.users".formatted(tenantDomain));
        cognitoTenantRepository.deleteGroup("tenant.%s.users".formatted(tenantDomain));
    }

    public void finishTenantAdminSetup(TenantCreate tenantCreate, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenantCreate::add::group::admin");
        cognitoTenantRepository.adminAddUserToGroup(tenantCreate.userPoolId(), signupUser.userName(), "tenant.main.admin");
        createTenantGroups(tenantCreate.userPoolId(), tenantCreate.PK());
        finishTenantUserSetup(tenantCreate, signupUser);
    }

    public void finishTenantUserSetup(TenantCreate tenantCreate, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenantCreate::add::group::tenantCreate::user");
        cognitoTenantRepository.adminAddUserToGroup(tenantCreate.userPoolId(), signupUser.userName(), "tenant.%s.users".formatted(tenantCreate.PK()));
        setUserCognitoAttributes(tenantCreate, signupUser);
    }

    public void setUserCognitoAttributes(TenantCreate tenantCreate, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenantCreate::config::cognito::user::attributes");
        cognitoTenantRepository.adminUpdateUserAttributes(tenantCreate.userPoolId(), signupUser.userName(), "custom:domain", tenantCreate.PK());
        cognitoTenantRepository.adminUpdateUserAttributes(tenantCreate.userPoolId(), signupUser.userName(), "custom:tenantId", tenantCreate.PK());
    }

    public void createTenantGroups(String userPoolId, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::create::group::tenant::users");
        cognitoTenantRepository.createGroup(userPoolId, "tenant.%s.users".formatted(tenantDomain));
    }

    private void sendSNSMessage(TenantCreate tenantCreate, SignupUser signupUser) {
        System.out.println("event::cognito::signup::request::tenantCreate::send::sns::message");
        PublishRequest request = tenantConverter.convertTenantAndSignupUserToSNSRequest(topicArn, tenantCreate, signupUser);
        snsClient
                .publish(request);

    }

}
