package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.function.Consumer;

@ApplicationScoped
public class PostConfirmationService {

    @Inject
    CognitoIdentityProviderClient cognitoClient;
    public CognitoSignupEvent process(CognitoSignupEvent input) {
        cognitoClient.adminAddUserToGroup(addGroupRequest(input.userPoolId(), input.userName(), "user"));
      /*  cognitoClient.adminAddUserToGroup(builder -> builder
                .groupName("user")
                .userPoolId(input.userPoolId())
                .username(input.userName()));*/
        return input;
    }

    private Consumer<AdminAddUserToGroupRequest.Builder> addGroupRequest(String userPoolId, String userName, String groupName) {
        return builder -> builder
                .groupName(groupName)
                .userPoolId(userPoolId)
                .username(userName);
    }
}
