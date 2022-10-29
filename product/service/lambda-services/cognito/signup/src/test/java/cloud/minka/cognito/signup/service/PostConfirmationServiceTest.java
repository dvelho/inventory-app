package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.*;

import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolEvent;
import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPreSignUpEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import org.gradle.internal.impldep.javax.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.PasswordPolicyType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolPolicyType;


import java.io.File;
import java.io.IOException;

import static cloud.minka.cognito.signup.model.cloudformation.TriggerSource.PostConfirmation_ConfirmSignUp;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class PostConfirmationServiceTest {

    PostConfirmationService postConfirmationService;
    CognitoTenantRepository cognitoTenantRepository;
    @Mock
    CognitoIdentityProviderClient cognitoClient;

    String userPoolId;
    @BeforeEach
    void setUp() {

        cognitoTenantRepository = new CognitoTenantRepository(cognitoClient);
        postConfirmationService = new PostConfirmationService(cognitoTenantRepository);
        CreateUserPoolRequest createUserPoolRequest = CreateUserPoolRequest.builder()
                .poolName("tenantPool")
                .policies(UserPoolPolicyType.builder()
                        .passwordPolicy(PasswordPolicyType.builder()
                                .minimumLength(8)
                                .requireLowercase(true)
                                .requireUppercase(true)
                                .requireNumbers(true)
                                .requireSymbols(true)
                                .temporaryPasswordValidityDays(7)
                                .build())
                        .build())
                .build();
        CreateUserPoolResponse createUserPoolResponse = cognitoClient.createUserPool(createUserPoolRequest);
        userPoolId = createUserPoolResponse.userPool().id();
    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void process() {
        ObjectMapper mapper = new ObjectMapper();

        System.out.print(userPoolId);
        try {
            CognitoSignupEvent cognitoSignupEvent = mapper
                    .readValue(new File("src/main/resources/event-post-confirmation.json"), CognitoSignupEvent.class);
            CognitoSignupEvent result = postConfirmationService.process(cognitoSignupEvent);
            assertEquals(result.userPoolId(), userPoolId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Read the json file
          /* CognitoSignupEvent cognitoSignupEvent = CognitoSignupEvent
                .builder()
                .version("1")
                .region("us-east-1")
                .userPoolId(userPoolId)
                .userName("testUser")
                .callerContext(CallerContext.builder()
                        .awsSdkVersion("awsSdkVersion")
                        .clientId("clientId")
                        .build())
                .triggerSource(PostConfirmation_ConfirmSignUp)
                .request(jsonNode)
                .response(ResponseSignup.builder()
                        .autoConfirmUser(true)
                        .autoVerifyEmail(true)
                        .autoVerifyPhone(true)
                        .build())
                .build();
     postConfirmationService.process(new CognitoSignupEvent(
                "tenantId",
                "tenantDomain",
                "tenantName",
                "tenantAdminEmail",
                new CallerContext("1","1"),
                PostConfirmation_ConfirmSignUp,
                jsonNode,
                new ResponseSignup("true","true","true")
        ));*/

    }
}