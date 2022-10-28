package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PostConfirmationService {

    @Inject
    CognitoIdentityProviderClient cognitoClient;
    public CognitoSignupEvent process(CognitoSignupEvent input) {
      /*  cognitoClient.adminAddUserToGroup(builder -> builder
                .groupName("user")
                .userPoolId(input.userPoolId())
                .username(input.userName()));*/
        return input;
    }
}
