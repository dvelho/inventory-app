package cloud.minka.user.welcome.controller;

import cloud.minka.user.welcome.service.WelcomeService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;


@Named("welcome-handler")
public final class WelcomeHandler implements RequestHandler<SNSEvent, JsonNode> {


    ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    WelcomeService welcomeService;

    @Inject
    public WelcomeHandler(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }


    @Override
    public JsonNode handleRequest(SNSEvent input, Context context) {


        System.out.println("event::user::welcome::request:" + mapper.valueToTree(input));
        if (true) {
            return mapper.createObjectNode().put("message", "Welcome to Minka");
        }
        input.getRecords().stream().map(SNSEvent.SNSRecord::getSNS).forEach(sns -> {
            String message = sns.getMessage();
            String subject = sns.getSubject();
            Map<String, SNSEvent.MessageAttribute> messageAttributes = sns.getMessageAttributes();
            String tenantId = messageAttributes.get("tenantId").getValue();
            String userName = messageAttributes.get("userName").getValue();
            String userEmail = messageAttributes.get("userEmail").getValue();
            String tenantDomain = messageAttributes.get("tenantDomain").getValue();
            boolean isTenantAdmin = Boolean.parseBoolean(messageAttributes.get("isTenantAdmin").getValue());
            if (isTenantAdmin) {
                welcomeService.sendWelcomeEmail(userEmail);
                return;
            }
            welcomeService.sendWelcomeEmail(userEmail);


        });
        return mapper.valueToTree(input);
    }

}
