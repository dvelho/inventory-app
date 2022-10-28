package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class PostConfirmationService {

    @Inject
    CognitoTenantRepository cognitoTenantRepository;
    public CognitoSignupEvent process(CognitoSignupEvent input) {
        cognitoTenantRepository.adminAddUserToGroup(input.userPoolId(), input.userName(), "tenant.user");
      /*  cognitoClient.adminAddUserToGroup(builder -> builder
                .groupName("user")
                .userPoolId(input.userPoolId())
                .username(input.userName()));*/
        return input;
    }


}
