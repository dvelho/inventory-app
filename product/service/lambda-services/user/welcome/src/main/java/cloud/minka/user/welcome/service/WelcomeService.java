package cloud.minka.user.welcome.service;

import cloud.minka.user.welcome.repository.SesEmailerService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sns.SnsClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;


@ApplicationScoped
public class WelcomeService {


    @Inject
    SesEmailerService sesEmailerService;

    @Inject
    SnsClient snsClient;
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

    public void sendWelcomeEmail(String email) {
        sesEmailerService.sendEmail(email, getHtmlTemplate(), emailText, emailSubject, emailFrom);
    }

    private String getHtmlTemplate() {
        // String resourcePath = "/welcome-email.html";
        try {
            InputStream ins = SesEmailerService.class.getResourceAsStream(emailHtml);
            assert ins != null;
            return new String(ins.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSubject() {
        return "";
    }

}
