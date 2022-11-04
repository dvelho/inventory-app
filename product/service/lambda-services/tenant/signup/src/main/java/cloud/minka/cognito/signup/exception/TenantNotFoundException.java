package cloud.minka.cognito.signup.exception;

public class TenantNotFoundException extends IllegalArgumentException {

    public TenantNotFoundException(String message) {
        super(message);
    }

}
