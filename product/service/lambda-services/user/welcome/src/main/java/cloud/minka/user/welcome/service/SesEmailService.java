package cloud.minka.user.welcome.service;

import software.amazon.awssdk.services.ses.SesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SesEmailService {

    SesClient sesClient;

    @Inject
    public SesEmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(String email, String htmlTemplate, String emailText, String emailSubject, String emailFrom) {
        sesClient.sendEmail(
                builder -> builder.destination(
                                destination -> destination.toAddresses(email))
                        .message(message -> message
                                .body(body -> body.text(text -> text.data(emailText))
                                        .html(html -> html.data(htmlTemplate)))

                                .subject(subject -> subject.data(emailSubject)))
                        .source(emailFrom)

        );
    }


}
