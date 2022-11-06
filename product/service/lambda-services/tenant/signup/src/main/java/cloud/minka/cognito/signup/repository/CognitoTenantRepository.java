package cloud.minka.cognito.signup.repository;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.function.Consumer;

@ApplicationScoped
public class CognitoTenantRepository {


    CognitoIdentityProviderClient cognitoClient;

    public CognitoTenantRepository(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
    }


    public boolean emailExists(String email, String userPoolId) {
        System.out.println("event::cognito::signup::request::tenant::email::exists:" + email);
        ListUsersRequest listUsersRequest = ListUsersRequest.builder()
                .userPoolId(userPoolId)
                .filter("email = \"" + email + "\"")
                .build();
        ListUsersResponse listUsersResponse = cognitoClient.listUsers(listUsersRequest);
        System.out.println("event::cognito::signup::request::tenant::email::exists:" + listUsersResponse.users().size());
        return listUsersResponse.users().size() > 0;
    }

    public void adminAddUserToGroup(String userPoolId, String userName, String groupName) {
        cognitoClient.adminAddUserToGroup(addGroupRequest(userPoolId, userName, groupName));
    }

    private Consumer<AdminAddUserToGroupRequest.Builder> addGroupRequest(String userPoolId, String userName, String groupName) {
        return builder -> builder
                .groupName(groupName)
                .userPoolId(userPoolId)
                .username(userName);
    }

    public void createGroup(String userPoolId, String groupName) {
        cognitoClient.createGroup(createGroupRequest(userPoolId, groupName));
    }

    private Consumer<CreateGroupRequest.Builder> createGroupRequest(String userPoolId, String groupName) {
        return builder -> builder
                .groupName(groupName)
                .userPoolId(userPoolId);
    }

    public void adminUpdateUserAttributes(String userPoolId, String userName, String attributeName, String attributeValue) {
        cognitoClient.adminUpdateUserAttributes(updateUserAttributesRequest(userPoolId, userName, attributeName, attributeValue));
    }

    private Consumer<AdminUpdateUserAttributesRequest.Builder> updateUserAttributesRequest(String userPoolId, String userName, String attributeName, String attributeValue) {
        return builder -> builder
                .userPoolId(userPoolId)
                .username(userName)
                .userAttributes(attribute(attributeName, attributeValue));

    }

    private Consumer<AttributeType.Builder> attribute(String attributeName, String attributeValue) {
        return builder -> builder
                .name(attributeName)
                .value(attributeValue);
    }


    private Consumer<CreateUserPoolRequest.Builder> createUserPoolRequest(String userPoolName) {
        return builder -> builder
                .poolName(userPoolName);
    }

    public void createUserPoolClient(String userPoolId, String clientName) {
        cognitoClient.createUserPoolClient(createUserPoolClientRequest(userPoolId, clientName));
    }

    private Consumer<CreateUserPoolClientRequest.Builder> createUserPoolClientRequest(String userPoolId, String clientName) {
        return builder -> builder
                .userPoolId(userPoolId)
                .clientName(clientName);
    }

    public CreateUserPoolResponse createUSerPool(String tenantDomain) {
        return cognitoClient.createUserPool(createUserPoolRequest(tenantDomain));
    }


    public void deleteUser(String userName) {
        cognitoClient.adminDeleteUser(deleteUserRequest(userName));
    }

    private Consumer<AdminDeleteUserRequest.Builder> deleteUserRequest(String userName) {
        return builder -> builder
                .username(userName);
    }

    public void deleteGroup(String groupName) {
        cognitoClient.deleteGroup(deleteGroupRequest(groupName));
    }

    private Consumer<DeleteGroupRequest.Builder> deleteGroupRequest(String groupName) {
        return builder -> builder
                .groupName(groupName);
    }
}