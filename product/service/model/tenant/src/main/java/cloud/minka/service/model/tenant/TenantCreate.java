package cloud.minka.service.model.tenant;

public record TenantCreate(
        String PK,
        String SK,
        String adminEmail,
        TenantStatus status,
        TenantType type,
        String userPoolId
) {
}
