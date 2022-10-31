package cloud.minka.user.welcome.repository;

import software.amazon.awssdk.services.ses.SesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SESRepository {

    SesClient sesClient;

    @Inject
    public SESRepository(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail() {
        sesClient.sendEmail(
                builder -> builder.destination(
                                destination -> destination.toAddresses("diogo.velho@mindera.com"))
                        .message(message -> message.body(body -> body.text(text -> text.data("Hello, world!")))
                                .subject(subject -> subject.data("Hello, world!"))));

    }
}
