package cloud.minka.user.welcome.converter;

import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.tenant.Tenant;
import cloud.minka.user.welcome.dto.MessageAttributes;
import cloud.minka.user.welcome.dto.NewUserMessage;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@RegisterForReflection(targets = {SignupUser.class, Tenant.class})
@ApplicationScoped
public class Converter {

    ObjectMapper mapper;

    @Inject
    public Converter(ObjectMapper mapper) {
        this.mapper = mapper.registerModule(new JodaModule());
    }

    public SignupUser signupUserFromJson(JsonNode signupUserJson) {
        return mapper.convertValue(signupUserJson, SignupUser.class);
    }

    public Tenant tenantFromJson(JsonNode tenantJson) {
        return mapper.convertValue(tenantJson, Tenant.class);
    }

    public NewUserMessage newUserMessageFromJson(JsonNode newUserMessageJson) {
        JsonNode signupUserJson = newUserMessageJson.get("signupUser");
        JsonNode tenantJson = newUserMessageJson.get("tenant");
        return new NewUserMessage(tenantFromJson(tenantJson), signupUserFromJson(signupUserJson));
    }

    public JsonNode bodyFromSQSMessage(SQSEvent.SQSMessage message) {
        try {
            return mapper.readTree(message.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public MessageAttributes messageAttributesFromJson(JsonNode messageAttributes) {
        String messageType = messageAttributes.get("MESSAGE_TYPE").get("Value").asText();
        String tenantDomain = messageAttributes.get("tenantDomain").get("Value").asText();
        boolean isTenantAdmin = messageAttributes.get("isTenantAdmin").get("Value").asBoolean();
        return new MessageAttributes(messageType, tenantDomain, isTenantAdmin);
    }

    public JsonNode messageFromBody(JsonNode message) {
        try {
            return mapper.readTree(message.asText());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
