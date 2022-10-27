package cloud.minka.cognito.signup.model.cloudformation;


import com.fasterxml.jackson.databind.JsonNode;

public record CognitoSignupEvent(
        String version,
        String region,
        String userPoolId,
        String userName,
        CallerContext callerContext,
        TriggerSource triggerSource,
        JsonNode request,
        ResponseSignup response) {

}
