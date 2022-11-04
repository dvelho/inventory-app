package cloud.minka.user.welcome.service;

import cloud.minka.user.welcome.repository.SesEmailService;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;


@ApplicationScoped
public class WelcomeService {


    SesEmailService sesEmailService;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;
    @ConfigProperty(name = "cloud.minka.tenant.sns.topic", defaultValue = "arn:aws:sns:eu-west-1:631674088803:dev1-tenant-signup-messages-minka-cloud")
    String topicArn;
    @ConfigProperty(name = "cloud.minka.email.welcome.from")
    String emailFrom;
    @ConfigProperty(name = "cloud.minka.email.welcome.subject")
    String emailSubject;
    @ConfigProperty(name = "cloud.minka.email.welcome.text")
    String emailText;
    @ConfigProperty(name = "cloud.minka.email.welcome.html")
    String emailHtml;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JodaModule());

    @Inject
    public WelcomeService(SesEmailService sesEmailService) {
        this.sesEmailService = sesEmailService;
    }

    public void sendWelcomeEmail(String email) {
        sesEmailService.sendEmail(email, getHtmlTemplate(), emailText, emailSubject, emailFrom);
    }

    private String getHtmlTemplate() {
        // String resourcePath = "/welcome-email.html";
        try {
            InputStream ins = SesEmailService.class.getResourceAsStream(emailHtml);
            assert ins != null;
            return new String(ins.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void process(SQSEvent.SQSMessage message) {
        JsonNode body = null;
        try {
            body = mapper.readTree(message.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode messageAttributes = body.get("MessageAttributes");
        String tenantId = messageAttributes.get("tenantId").get("Value").asText();
        String userName = messageAttributes.get("userName").get("Value").asText();
        String userEmail = messageAttributes.get("userEmail").get("Value").asText();
        String tenantDomain = messageAttributes.get("tenantDomain").get("Value").asText();
        boolean isTenantAdmin = Boolean.parseBoolean(messageAttributes.get("isTenantAdmin").get("Value").asText());
        String messageType = messageAttributes.get("SNS_MESSAGE_TYPE").get("Value").asText();
        System.out.println("event::user::welcome::request:" + body);

        //need  refactor PostConfirmationService body should contain all data


       /* JsonNode jsonNode = message.getRecords().get(0).getBody();
        SQSEvent.MessageAttribute messageAttributes = message.getRecords().get(0).getMessageAttributes();
        message = message.get("MessageAttributes");
        String tenantId = message.get("tenantId").asText();
        String userName = message.get("userName").asText();
        String userEmail = message.get("userEmail").asText();
        String tenantDomain = message.get("tenantDomain").asText();
        boolean isTenantAdmin = message.get("isTenantAdmin").asBoolean();
        if (isTenantAdmin) {
            sendWelcomeEmail(userEmail);
            return;
        }
        sendWelcomeEmail(userEmail);*/
    }
}
