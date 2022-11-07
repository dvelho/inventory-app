package cloud.minka.user.welcome.service;

import cloud.minka.user.welcome.converter.Converter;
import cloud.minka.user.welcome.dto.MessageAttributes;
import cloud.minka.user.welcome.dto.NewUserMessage;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
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

    SesEmailService sesEmailService;
    ObjectMapper mapper;
    Converter converter;

    @Inject
    public WelcomeService(SesEmailService sesEmailService, ObjectMapper mapper, Converter converter) {
        this.sesEmailService = sesEmailService;
        this.mapper = mapper.registerModule(new JodaModule());
        this.converter = converter;
    }


    public void process(SQSEvent.SQSMessage message) {

        JsonNode body = converter.bodyFromSQSMessage(message);
        JsonNode messageAttributes = body.get("MessageAttributes");
        MessageAttributes attributes = converter.messageAttributesFromJson(messageAttributes);
        JsonNode messageBody = converter.messageFromBody(body.get("Message"));
        NewUserMessage newUserMessage = converter.newUserMessageFromJson(messageBody);
        System.out.println("event::user::welcome::request:" + body);
        if (attributes.isTenantAdmin() == true) {
            sendWelcomeEmail(newUserMessage.signupUser().email());
            return;
        }
        sendWelcomeEmail(newUserMessage.signupUser().email());

    }

    public void sendWelcomeEmail(String email) {
        sesEmailService.sendEmail(email, getHtmlTemplate(), emailText, emailSubject, emailFrom);
    }

    private String getHtmlTemplate() {
        try {
            InputStream ins = SesEmailService.class.getResourceAsStream(emailHtml);
            assert ins != null;
            return new String(ins.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
