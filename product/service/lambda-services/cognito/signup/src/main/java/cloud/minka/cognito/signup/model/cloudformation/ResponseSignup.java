package cloud.minka.cognito.signup.model.cloudformation;

public record ResponseSignup(String autoConfirmUser, String autoVerifyEmail, String autoVerifyPhone) {
}
