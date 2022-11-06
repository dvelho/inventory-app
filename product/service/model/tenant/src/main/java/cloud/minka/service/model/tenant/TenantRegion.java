package cloud.minka.service.model.tenant;

public enum TenantRegion {
    EU_WEST_1("europe"),
    US_EAST_1("america");

    private final String description;

    TenantRegion(String description) {
        this.description = description;
    }

    public static TenantRegion fromString(String name) {
        for (TenantRegion region : TenantRegion.values()) {
            if (region.name().equals(name)) {
                return region;
            }
        }
        return null;
    }

    public String description() {
        return description;
    }


}
