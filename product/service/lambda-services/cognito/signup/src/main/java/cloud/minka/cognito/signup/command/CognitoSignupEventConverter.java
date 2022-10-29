package cloud.minka.cognito.signup.command;

import cloud.minka.cognito.signup.model.cloudformation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CognitoSignupEventConverter {
    @Inject
    public   ObjectMapper mapper;
        public  CognitoSignupEvent response(CognitoSignupEvent input) {
            return CognitoSignupEventBuilder.builder()
                    .version(input.version())
                    .region(input.region())
                    .userPoolId(input.userPoolId())
                    .userName(input.userName())
                    .callerContext(input.callerContext())
                    .triggerSource(input.triggerSource())
                    .request(input.request())
                    .response(mapper.valueToTree(ResponseSignupBuilder.builder()
                            .autoConfirmUser("true")
                            .autoVerifyEmail("true")
                            .autoVerifyPhone("true")
                            .build()))
                    .build();
        }

    public  String toJson(CognitoSignupEvent input) {
        return mapper.valueToTree(response(input)).toString();
    }

    public  CognitoSignupEvent fromJson(String input) {
        return mapper.convertValue(input, CognitoSignupEvent.class);
    }

    public  CognitoSignupEvent responsePostSignup(CognitoSignupEvent input) {

        return CognitoSignupEventBuilder.builder()
                .version(input.version())
                .region(input.region())
                .userPoolId(input.userPoolId())
                .userName(input.userName())
                .callerContext(input.callerContext())
                .triggerSource(input.triggerSource())
                .request(input.request())
                .response(mapper.createObjectNode())
                .build();
    }
}
