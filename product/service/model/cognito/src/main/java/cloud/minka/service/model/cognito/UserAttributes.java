package cloud.minka.service.model.cognito;

public record UserAttributes(
        String sub,
        String email_verified,
        String cognito_user_status,
        String identities,
        String email) {
}

