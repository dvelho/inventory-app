package cloud.minka.cognito.signup.converter;


import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.tenant.SignupUser;
import cloud.minka.service.model.tenant.Tenant;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.soabase.recordbuilder.core.RecordBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@RecordBuilder.Include({
        CognitoSignupEvent.class, Tenant.class    // generates a record builder for ImportedRecord
})

@ApplicationScoped
public class Converter {
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


    public Tenant convertGetItemResponseToTenant(GetItemResponse tenant) {
        Map<String, AttributeValue> tenantMap = tenant.item();
        return new Tenant(
                tenantMap.get("PK").s(),
                tenantMap.get("SK").s(),
                tenantMap.get("adminEmail").s(),
                TenantStatus.valueOf(tenantMap.get("status").s()),
                TenantType.valueOf(tenantMap.get("type").s()),
                tenantMap.get("userPoolId").s()
        );
    }

    public SignupUser convertCognitoSignupEventToSignupUser(CognitoSignupEvent input, boolean isTenantAdmin) {
        return new SignupUser(
                input.userName(),
                input.request().get("userAttributes").get("email").asText(),
                isTenantAdmin
        );
    }

    public String convertTenantAndSignupUserToSNSMessage(Tenant tenant, SignupUser signupUser) {
        JsonNode tenantJson = mapper.valueToTree(tenant);
        JsonNode signupUserJson = mapper.valueToTree(signupUser);
        return "{\"tenant\": " + tenantJson.toString() + ", \"signupUser\": " + signupUserJson.toString() + "}";

    }
}
