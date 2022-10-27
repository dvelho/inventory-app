package cloud.minka.cognito.signup.controller;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.ResponseSignup;
import cloud.minka.cognito.signup.service.ProcessingService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.inject.Inject;
import javax.inject.Named;

@Named("signup-handler")
public final class SignupHandler implements RequestHandler<CognitoSignupEvent, ResponseSignup> {

    @Inject
    ProcessingService service;

    @Override
    public ResponseSignup handleRequest(CognitoSignupEvent input, Context context) {
        return service.process(input);
    }
}
