package cloud.minka.cognito.signup.model.cloudformation;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record ResponseSignup(String autoConfirmUser, String autoVerifyEmail, String autoVerifyPhone) {
}
