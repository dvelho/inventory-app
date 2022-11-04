package cloud.minka.service.model.tenant;

public record Tenant(
        String PK,
        String SK,
        String adminEmail,
        TenantStatus status,
        TenantType type,
        String userPoolId
) {
}
