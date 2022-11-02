package cloud.minka.user.welcome.controller;

import cloud.minka.user.welcome.service.WelcomeService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;


@Named("welcome-handler")
public final class WelcomeHandler implements RequestHandler<SNSEvent, JsonNode> {


    private static final String LAMBDA_TRACE_HEADER_PROP = "com.amazonaws.xray.traceHeader";
    ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    WelcomeService welcomeService;

    @Inject
    public WelcomeHandler(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }


    @Override
    public JsonNode handleRequest(SNSEvent input, Context context) {
        String aws_region = System.getenv("AWS_REGION");
        String x_amzn_trace_id = System.getenv("_X_AMZN_TRACE_ID");
        System.out.println("Lambda AWS Region: " + aws_region);
        System.out.println("Lambda _X_AMZN_TRACE_ID: " + x_amzn_trace_id);
        String env = "--ENV--\n"
                + "AWS_REGION: " + aws_region + "\n"
                + "_X_AMZN_TRACE_ID: " + x_amzn_trace_id + "\n"
                + "Trace Property: " + System.getProperty(LAMBDA_TRACE_HEADER_PROP) + "\n";

        Subsegment subsegment = AWSXRay.beginSubsegment("welcome-handler");
        System.out.println("event::user::welcome::request:" + mapper.valueToTree(input));
        input.getRecords().stream().map(SNSEvent.SNSRecord::getSNS).forEach(sns -> {
            String message = sns.getMessage();
            String subject = sns.getSubject();
            Map<String, SNSEvent.MessageAttribute> messageAttributes = sns.getMessageAttributes();
            String tenantId = messageAttributes.get("tenantId").getValue();
            String user = messageAttributes.get("user").getValue();
            welcomeService.sendWelcomeEmail(user);
        });
        AWSXRay.endSubsegment();
        return mapper.valueToTree(input);
    }

}
