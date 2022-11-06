package cloud.minka.service.model.tenant;

import cloud.minka.service.model.entity.EntityType;

public record TenantCreate(
        String PK,
        String SK,
        String adminEmail,
        TenantStatus status,
        TenantType type,
        String userPoolId
) {
    private static final EntityType entityType = EntityType.TENANT;

    public TenantCreate(String PK, String SK, String adminEmail, TenantStatus status, TenantType type, String userPoolId) {
        this.PK = entityType.prefixPK() + PK.replace(entityType.prefixPK(), "");
        this.SK = entityType.prefixSK() + SK.replace(entityType.prefixSK(), "");
        this.adminEmail = adminEmail;
        this.status = status;
        this.type = type;
        this.userPoolId = userPoolId;
    }
}
