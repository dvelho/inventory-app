package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.ResponseSignup;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class ProcessingService {

    public static final String CAN_ONLY_GREET_NICKNAMES = "Sorry I Can only greet nicknames";

    public ResponseSignup process(CognitoSignupEvent input) {
       /* if (input.getName().equals("Stuart")) {
            throw new IllegalArgumentException(CAN_ONLY_GREET_NICKNAMES);
        }
        String result = input.getGreeting() + " estupido " + input.getName();
        OutputObject out = new OutputObject();
        out.setResult(result);*/
        return new ResponseSignup(false, false, false);
    }
}
