package cloud.minka.cognito.signup.repository;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.function.Consumer;

@ApplicationScoped
public class CognitoTenantRepository {


    CognitoIdentityProviderClient cognitoClient;

    public CognitoTenantRepository(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
    }




    public void adminAddUserToGroup(String userPoolId, String userName, String user) {
        cognitoClient.adminAddUserToGroup(addGroupRequest(userPoolId, userName, user));
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
    //Create UserPoolClient
    public void createUserPoolClient(String userPoolId, String clientName) {
        cognitoClient.createUserPoolClient(createUserPoolClientRequest(userPoolId, clientName));
    }

    private Consumer<CreateUserPoolClientRequest.Builder> createUserPoolClientRequest(String userPoolId, String clientName) {
        return builder -> builder
                .userPoolId(userPoolId)
                .clientName(clientName);
    }

    //CreateUserPoolClientRequest

    //CreateUserPoolDomainRequest
    //CreateUserPoolClientRequest


}