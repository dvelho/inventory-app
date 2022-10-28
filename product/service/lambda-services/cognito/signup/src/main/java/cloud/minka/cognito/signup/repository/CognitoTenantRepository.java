package cloud.minka.cognito.signup.repository;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateGroupRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.function.Consumer;

@ApplicationScoped
public class CognitoTenantRepository {

    @Inject
    CognitoIdentityProviderClient cognitoClient;

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


}
