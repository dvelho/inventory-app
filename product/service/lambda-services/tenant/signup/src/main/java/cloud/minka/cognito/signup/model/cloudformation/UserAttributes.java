package cloud.minka.cognito.signup.model.cloudformation;

public record UserAttributes(
        String sub,
        String email_verified,
        String cognito_user_status,
        String identities,
        String email) {
}

