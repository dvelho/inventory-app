package cloud.minka.service.model.tenant;

import cloud.minka.service.model.location.Address;

public record TenantConfiguration(
        String PK,
        String SK,
        Address address,
        TenantStatus status,
        TenantStylePersonalization stylePersonalization,
        TenantRegion region
) {
}
