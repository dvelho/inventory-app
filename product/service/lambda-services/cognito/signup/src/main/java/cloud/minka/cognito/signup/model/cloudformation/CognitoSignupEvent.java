package cloud.minka.cognito.signup.model.cloudformation;

public record CognitoSignupEvent(
        String version,
        String region,
        String userPoolId,
        String userName,
        CallerContext callerContext,
        TriggerSource triggerSource,
        RequestSignup request,
        ResponseSignup response) {

}
