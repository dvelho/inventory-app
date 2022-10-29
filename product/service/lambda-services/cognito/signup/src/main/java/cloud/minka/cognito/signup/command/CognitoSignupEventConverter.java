package cloud.minka.cognito.signup.command;

import cloud.minka.cognito.signup.model.cloudformation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CognitoSignupEventConverter {
    @Inject
    public   ObjectMapper mapper;
        public  CognitoSignupEvent response(CognitoSignupEvent input) {
            String response = "{\"autoConfirmUser\": \"true\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}";
            JsonNode responseJson = null;
            try {
                 responseJson = mapper.readTree(response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            return CognitoSignupEventBuilder.builder()
                    .version(input.version())
                    .region(input.region())
                    .userPoolId(input.userPoolId())
                    .userName(input.userName())
                    .callerContext(input.callerContext())
                    .triggerSource(input.triggerSource())
                    .request(input.request())
                    .response(responseJson)
                    .build();
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
