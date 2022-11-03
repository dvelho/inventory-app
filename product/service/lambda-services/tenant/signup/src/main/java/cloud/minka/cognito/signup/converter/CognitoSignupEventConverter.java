package cloud.minka.cognito.signup.converter;


import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.tenant.Tenant;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.soabase.recordbuilder.core.RecordBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@RecordBuilder.Include({
        CognitoSignupEvent.class    // generates a record builder for ImportedRecord
})

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

    public Tenant toTenant(CognitoSignupEvent input) {
        return new Tenant(
                input.userName(),
                input.userName(),
                input.userName(),
                input.userName(),
                TenantStatus.ACTIVE,
                TenantType.HOSTED
        );
    }
}
