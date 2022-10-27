package cloud.minka.cognito.signup.controller;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.TriggerSource;
import cloud.minka.cognito.signup.service.ProcessingService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;


import javax.inject.Inject;
import javax.inject.Named;

@Named("signup-handler")
public final class SignupHandler implements RequestHandler<CognitoSignupEvent, CognitoSignupEvent> {

    @Inject
    ProcessingService service;

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public CognitoSignupEvent handleRequest(CognitoSignupEvent input, Context context) {
        System.out.println("event::cognito::signup::request:" + mapper.valueToTree(input));
        //System.out.println("Context: " + context.);
        TriggerSource triggerSource = input.triggerSource();
        try {
            return switch (triggerSource) {
                case PreSignUp_ExternalProvider, PreSignUp_Registration -> service.processPreSignup(input);
                case PostConfirmation_ConfirmSignUp -> service.processPreSignup(input);
                default -> throw new IllegalArgumentException("Unsupported trigger source: " + triggerSource);
            };
        } catch (Exception e) {
            System.out.println("event::cognito::signup::error:" + e.getMessage());
            throw e;
        }

       // return service.process(input);
    }
}
