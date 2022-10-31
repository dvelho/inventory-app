package cloud.minka.cognito.signup.converter;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEventBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CognitoSignupEventConverter {
    @Inject
    public ObjectMapper mapper;

    public CognitoSignupEvent response(CognitoSignupEvent input) {
        String response = "{\"autoConfirmUser\": \"false\", \"autoVerifyEmail\": \"false\", \"autoVerifyPhone\": \"false\"}";
        JsonNode responseJson;
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

    public CognitoSignupEvent responsePostSignup(CognitoSignupEvent input) {

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
