package cloud.minka.service.model.tenant;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record Tenant(
        String PK,
        String SK,
        String adminEmail,
        TenantStatus status,
        TenantType type,
        String userPoolId
) {
}
