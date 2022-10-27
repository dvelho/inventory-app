package cloud.minka.cognito.signup.model.cloudformation;

public record ResponseSignup(Boolean autoConfirmUser, Boolean autoVerifyEmail, Boolean autoVerifyPhone) {
}
