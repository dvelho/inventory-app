package cloud.minka.cognito.signup.model.cloudformation;

public enum TenantStatus {
    ACTIVE,
    INACTIVE,
    DELETED,
    PENDING_CONFIGURATION,
    FAILED,
    UNKNOWN
}
