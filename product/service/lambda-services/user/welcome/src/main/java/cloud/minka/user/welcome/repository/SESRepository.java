package cloud.minka.user.welcome.repository;

import software.amazon.awssdk.services.ses.SesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class SESRepository {

    SesClient sesClient;

    @Inject
    public SESRepository(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(String email) {
        //read html template


        sesClient.sendEmail(
                builder -> builder.destination(
                                destination -> destination.toAddresses(email))
                        .message(message -> message
                                .body(body -> body.text(text -> text.data("Welcome to minka cloud!")))
                                .body(body -> body.html(html -> html.data(getHtmlTemplate())))
                                .subject(subject -> subject.data("\uD83D\uDCE6 \uD83C\uDF1F ☁ Welcome to minka.cloud ☁ \uD83C\uDF1F \uD83D\uDCE6")))
                        .source("info@minka.cloud")
        );
        //test

    }

    private String getHtmlTemplate() {

        String resourcePath = "/welcome-email.html";
        System.out.println("event::cognito::signup::request::tenant::domain::free::provider::check");
        String content = null;
        try {
            InputStream ins = SESRepository.class.getResourceAsStream(resourcePath);
            assert ins != null;

            content = new String(ins.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return content;
    }
}
