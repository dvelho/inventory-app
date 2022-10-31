package cloud.minka.cognito.signup.model.cloudformation;

//import com.amazonaws.regions.Regions;

public record Tenant(
        String tenantDomain,
        String tenantId,
        String tenantName,
        String tenantAdminEmail,
        TenantStatus tenantStatus,
        TenantType tenantType
) {
}
