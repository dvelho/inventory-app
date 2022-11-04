package cloud.minka.service.model.tenant;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum TenantStatus {
    ACTIVE,
    INACTIVE,
    DELETED,
    PENDING_CONFIGURATION,
    FAILED,
    UNKNOWN
}
