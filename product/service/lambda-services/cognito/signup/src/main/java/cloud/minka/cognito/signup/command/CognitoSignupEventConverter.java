package cloud.minka.cognito.signup.command;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEventBuilder;
import cloud.minka.cognito.signup.model.cloudformation.ResponseSignup;

public class CognitoSignupEventConverter {

        public static CognitoSignupEvent response(CognitoSignupEvent input) {
            return CognitoSignupEventBuilder.builder()
                    .version(input.version())
                    .region(input.region())
                    .userPoolId(input.userPoolId())
                    .userName(input.userName())
                    .callerContext(input.callerContext())
                    .triggerSource(input.triggerSource())
                    .request(input.request())
                    .response(new ResponseSignup("true", "false", "false"))
                    .build();
        }
}
