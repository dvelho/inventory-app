package cloud.minka.user.welcome.service;

import cloud.minka.user.welcome.repository.CognitoTenantRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sns.SnsClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@ApplicationScoped
public class WelcomeService {


    @Inject
    CognitoTenantRepository cognitoTenantRepository;

    @Inject
    SnsClient snsClient;
    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    @ConfigProperty(name = "cloud.minka.tenant.sns.topic", defaultValue = "arn:aws:sns:eu-west-1:631674088803:dev1-tenant-signup-messages-minka-cloud")
    String topicArn;


       /*  private void sendSNSMessage(CognitoSignupEvent input) {
   System.out.println("event::cognito::signup::request::tenant::send::sns::message");
        snsClient
                .publish(builder -> builder.topicArn(topicArn)
                        .message("New user signup for tenant %s".formatted(input.request().get("userAttributes").get("email").asText()))
                        .subject("NEW_USER_SIGNUP")
                        .messageAttributes(new HashMap<>() {{
                            put("SNS_MESSAGE_TYPE_ATTRIBUTE", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(input.request()
                                            .get("userAttributes")
                                            .get("email").asText().split("@")[1])
                                    .build());
                            put("tenant", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(input.request()
                                            .get("userAttributes")
                                            .get("email").asText().split("@")[1])
                                    .build());
                            put("user", MessageAttributeValue.builder()
                                    .dataType("String").stringValue(input.request()
                                            .get("userAttributes")
                                            .get("email").asText())
                                    .build());
                        }})
                        .build());
    }*/

}
