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
    @Inject
    WelcomeService welcomeService;

    @Override
    public JsonNode handleRequest(SNSEvent input, Context context) {

        System.out.println("event::cognito::signup::request:" + mapper.valueToTree(input));
        input.getRecords().stream().map(SNSEvent.SNSRecord::getSNS).forEach(sns -> {
            String message = sns.getMessage();
            String subject = sns.getSubject();
            Map<String, SNSEvent.MessageAttribute> messageAttributes = sns.getMessageAttributes();
            String tenantId = messageAttributes.get("tenantId").getValue();
            String user = messageAttributes.get("user").getValue();
            welcomeService.sendWelcomeEmail(user);
        });

        return mapper.valueToTree(input);
    }

}
