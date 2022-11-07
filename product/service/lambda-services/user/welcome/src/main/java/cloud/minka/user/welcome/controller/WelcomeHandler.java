package cloud.minka.user.welcome.controller;

import cloud.minka.user.welcome.service.WelcomeService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import javax.inject.Inject;
import javax.inject.Named;


@Named("welcome-handler")
public final class WelcomeHandler implements RequestHandler<SQSEvent, JsonNode> {


    ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    WelcomeService welcomeService;

    @Inject
    public WelcomeHandler(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }


    @Override
    public JsonNode handleRequest(SQSEvent input, Context context) {
        input.getRecords().forEach(record -> welcomeService.process(record));
        return mapper.valueToTree(input);
    }

}
