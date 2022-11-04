package cloud.minka.cognito.signup.converter;


import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.tenant.Tenant;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.soabase.recordbuilder.core.RecordBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@RecordBuilder.Include({
        CognitoSignupEvent.class, Tenant.class    // generates a record builder for ImportedRecord
})
@RegisterForReflection(targets = {CognitoSignupEvent.class, Tenant.class, SignupUser.class})
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

    public PutItemRequest convertTenantToPutItemRequest(String table, Tenant tenant) {
        return PutItemRequest.builder()
                .tableName(table)
                .item(Map.of(
                        "PK", AttributeValue.builder().s(tenant.PK()).build(),
                        "SK", AttributeValue.builder().s(tenant.SK()).build(),
                        "adminEmail", AttributeValue.builder().s(tenant.adminEmail()).build(),
                        "status", AttributeValue.builder().s(tenant.status().name()).build(),
                        "type", AttributeValue.builder().s(tenant.type().name()).build(),
                        "userPoolId", AttributeValue.builder().s(tenant.userPoolId()).build()
                ))
                .build();
    }

    public SignupUser convertCognitoSignupEventToSignupUser(CognitoSignupEvent input, boolean isTenantAdmin) {
        return new SignupUser(
                input.userName(),
                input.request().get("userAttributes").get("email").asText(),
                isTenantAdmin
        );
    }

    public String convertTenantAndSignupUserToSNSMessage(Tenant tenant, SignupUser signupUser) {
        System.out.println("AAAAAAAAAtenant: " + tenant);
        System.out.println("AAAAAAAAAsignupUser: " + signupUser);
        JsonNode signupUserJson = mapper.valueToTree(signupUser);
        System.out.println("AAAAAAAAAsignupUserJson: " + signupUserJson);
        JsonNode tenantJson = mapper.valueToTree(tenant);
        System.out.println("AAAAAAAAAtenantJson: " + tenantJson);
        ;
        return "{\"tenant\": " + tenantJson.toString() + ", \"signupUser\": " + signupUserJson.toString() + "}";

    }
}
