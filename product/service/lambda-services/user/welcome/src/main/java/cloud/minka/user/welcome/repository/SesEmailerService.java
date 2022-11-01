package cloud.minka.user.welcome.repository;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.ses.SesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SesEmailerService {

    SesClient sesClient;

    @ConfigProperty(name = "cloud.minka.email.welcome.from")
    String emailFrom;

    @Inject
    public SesEmailerService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(String email, String htmlTemplate, String emailSubject) {
        sesClient.sendEmail(
                builder -> builder.destination(
                                destination -> destination.toAddresses(email))
                        .message(message -> message
                                .body(body -> body.text(text -> text.data("Welcome to minka cloud!")))
                                .body(body -> body.html(html -> html.data(htmlTemplate)))
                                .subject(subject -> subject.data(emailSubject)))
                        .source(emailFrom)

        );
    }


}
