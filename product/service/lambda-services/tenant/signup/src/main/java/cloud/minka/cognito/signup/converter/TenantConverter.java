package cloud.minka.cognito.signup.converter;


import cloud.minka.service.model.cognito.CognitoSignupEvent;
import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.tenant.TenantCreate;
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
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@RecordBuilder.Include({
        CognitoSignupEvent.class  // generates a record builder for ImportedRecord
})
@RegisterForReflection(targets = {CognitoSignupEvent.class, TenantCreate.class, SignupUser.class})

@ApplicationScoped
public class TenantConverter {
    ObjectMapper mapper;

    @Inject
    public TenantConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }


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


    public TenantCreate convertGetItemResponseToTenant(GetItemResponse tenant) {
        Map<String, AttributeValue> tenantMap = tenant.item();
        return new TenantCreate(
                tenantMap.get("PK").s(),
                tenantMap.get("SK").s(),
                tenantMap.get("adminEmail").s(),
                TenantStatus.valueOf(tenantMap.get("status").s()),
                TenantType.valueOf(tenantMap.get("type").s()),
                tenantMap.get("userPoolId").s()
        );
    }

    public PutItemRequest convertTenantToPutItemRequest(String table, TenantCreate tenantCreate) {
        return PutItemRequest.builder()
                .tableName(table)
                .item(Map.of(
                        "PK", AttributeValue.builder().s(tenantCreate.PK()).build(),
                        "SK", AttributeValue.builder().s(tenantCreate.SK()).build(),
                        "adminEmail", AttributeValue.builder().s(tenantCreate.adminEmail()).build(),
                        "status", AttributeValue.builder().s(tenantCreate.status().name()).build(),
                        "type", AttributeValue.builder().s(tenantCreate.type().name()).build(),
                        "userPoolId", AttributeValue.builder().s(tenantCreate.userPoolId()).build()
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

    public String convertTenantAndSignupUserToSNSMessage(TenantCreate tenantCreate, SignupUser signupUser) {
        JsonNode signupUserJson = mapper.valueToTree(signupUser);
        JsonNode tenantJson = mapper.valueToTree(tenantCreate);
        return "{\"tenantCreate\": " + tenantJson.toString() + ", \"signupUser\": " + signupUserJson.toString() + "}";

    }

    public PublishRequest convertTenantAndSignupUserToSNSRequest(String topicArn, TenantCreate tenantCreate, SignupUser signupUser) {
        return PublishRequest.builder()
                .topicArn(topicArn)
                .message(convertTenantAndSignupUserToSNSMessage(tenantCreate, signupUser))
                .subject("NEW_USER_SIGNUP")
                .messageAttributes(new HashMap<>() {{
                    put("MESSAGE_TYPE", MessageAttributeValue.builder()
                            .dataType("String").stringValue("NEW_USER_SIGNUP").build());
                    put("tenantDomain", MessageAttributeValue.builder()
                            .dataType("String").stringValue(tenantCreate.PK())
                            .build());
                    put("isTenantAdmin", MessageAttributeValue.builder()
                            .dataType("String").stringValue(String.valueOf(signupUser.isTenantAdmin()))
                            .build());
                }})
                // .messageDeduplicationId(UUID.randomUUID().toString())
                .messageGroupId(signupUser.userName())
                .build();
    }
}
