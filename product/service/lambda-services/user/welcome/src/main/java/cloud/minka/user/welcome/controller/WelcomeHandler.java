package cloud.minka.user.welcome.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import javax.inject.Named;

@Named("welcome-handler")
public final class WelcomeHandler implements RequestHandler<SNSEvent, JsonNode> {


    ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    @Override
    public JsonNode handleRequest(SNSEvent input, Context context) {
        //
        System.out.println("event::cognito::signup::request:" + mapper.valueToTree(input));
        return mapper.valueToTree(input);
    }

}
