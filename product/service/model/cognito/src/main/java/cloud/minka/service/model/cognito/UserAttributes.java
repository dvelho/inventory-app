package cloud.minka.service.model.cognito;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record UserAttributes(
        String sub,
        String email_verified,
        String cognito_user_status,
        String identities,
        String email) {
}

