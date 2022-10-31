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
        String message = input.getRecords().get(0).getSNS().getMessage();
        String subject = input.getRecords().get(0).getSNS().getSubject();
        System.out.println("event::cognito::signup::message:" + message);
        System.out.println("event::cognito::signup::subject:" + subject);
        Map<String, SNSEvent.MessageAttribute> messageAttributes = input.getRecords().get(0).getSNS().getMessageAttributes();
        System.out.println("event::cognito::signup::messageAttributes:" + messageAttributes);
        String tenantId = messageAttributes.get("tenantId").getValue();
        System.out.println("event::cognito::signup::tenantId:" + tenantId);
        String user = messageAttributes.get("user").getValue();
        System.out.println("event::cognito::signup::user:" + user);
        welcomeService.sendEmail(user);
        return mapper.valueToTree(input);
    }

}
