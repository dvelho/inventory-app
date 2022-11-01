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


    public void sendWelcomeEmail(String email) {
        sesEmailerService.sendEmail(email, getHtmlTemplate(), getSubject());
    }

    private String getHtmlTemplate() {
        String resourcePath = "/welcome-email.html";
        System.out.println("event::cognito::signup::request::tenant::domain::free::provider::check");
        try {
            InputStream ins = SesEmailerService.class.getResourceAsStream(resourcePath);
            assert ins != null;
            return new String(ins.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getSubject() {
        return "\uD83D\uDCE6 \uD83C\uDF1F ☁ Welcome to minka.cloud ☁ \uD83C\uDF1F \uD83D\uDCE6";
    }

}
