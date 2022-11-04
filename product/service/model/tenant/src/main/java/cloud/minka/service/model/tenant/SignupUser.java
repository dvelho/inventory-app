package cloud.minka.service.model.tenant;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record SignupUser(String userName, String email, boolean isTenantAdmin) {

}
