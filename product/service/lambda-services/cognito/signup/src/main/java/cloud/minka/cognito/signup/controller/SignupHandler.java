package cloud.minka.cognito.signup.controller;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.TriggerSource;
import cloud.minka.cognito.signup.service.PostConfirmationService;
import cloud.minka.cognito.signup.service.PreSignupProcessingService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.inject.Inject;
import javax.inject.Named;

@Named("signup-handler")
public final class SignupHandler implements RequestHandler<CognitoSignupEvent, CognitoSignupEvent> {

    @Inject
    PreSignupProcessingService preSignupProcessingService;

    @Inject
    PostConfirmationService postConfirmationService;

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public CognitoSignupEvent handleRequest(CognitoSignupEvent input, Context context) {
        System.out.println("event::cognito::signup::request:" + mapper.valueToTree(input));
        TriggerSource triggerSource = input.triggerSource();
        try {
            return switch (triggerSource) {
                case PreSignUp_ExternalProvider,  PreSignUp_SignUp -> preSignupProcessingService.process(input);
                case PostConfirmation_ConfirmSignUp -> postConfirmationService.process(input);
                default -> throw new IllegalArgumentException("Unsupported trigger source: " + triggerSource);
            };
        } catch (Exception e) {
            System.out.println("event::cognito::signup::error:" + e.getMessage());
            throw e;
        }
    }

}
