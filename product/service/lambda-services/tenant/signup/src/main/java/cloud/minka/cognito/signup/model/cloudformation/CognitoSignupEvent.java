package cloud.minka.cognito.signup.model.cloudformation;


import com.fasterxml.jackson.databind.JsonNode;
import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record CognitoSignupEvent(
        String version,
        String region,
        String userPoolId,
        String userName,
        CallerContext callerContext,
        TriggerSource triggerSource,
        JsonNode request,
        JsonNode response) {


}
