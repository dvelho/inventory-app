package cloud.minka.cognito.signup.model.cloudformation;

public record CognitoSignupEvent(String region,
                                 String userPoolId,
                                 String userName,
                                 CallerContext callerContext,
                                 String triggerSource,
                                 Request request,
                                 ResponseSignup responseSignup) {

}
