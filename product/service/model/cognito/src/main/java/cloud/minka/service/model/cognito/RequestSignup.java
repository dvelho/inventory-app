package cloud.minka.service.model.cognito;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record RequestSignup(UserAttributes userAttributes) {
}
