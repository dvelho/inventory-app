package cloud.minka.user.welcome.repository;

import software.amazon.awssdk.services.ses.SesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SesEmailerService {

    SesClient sesClient;


    @Inject
    public SesEmailerService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(String email, String htmlTemplate, String emailSubject, String emailFrom) {
        sesClient.sendEmail(
                builder -> builder.destination(
                                destination -> destination.toAddresses(email))
                        .message(message -> message
                                .body(body -> body.text(text -> text.data("Welcome to minka.cloud! Are you ready to start scanning??")))
                                .body(body -> body.html(html -> html.data(htmlTemplate)))
                                .subject(subject -> subject.data(emailSubject)))
                        .source(emailFrom)

        );
    }


}
