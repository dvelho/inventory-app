package cloud.minka.service.model.tenant;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum TenantType {
    HOSTED,
    CUSTOM
}
