package cloud.minka.product.service.common.security;

import io.quarkus.amazon.lambda.http.LambdaAuthenticationRequest;
import io.quarkus.amazon.lambda.http.model.AwsProxyRequest;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;

public interface LambdaIdentityProvider extends IdentityProvider<LambdaAuthenticationRequest> {
    /**
     * You must override this method unless you directly override
     * IdentityProvider.authenticate
     *
     * @param event
     * @return
     */
    default SecurityIdentity authenticate(AwsProxyRequest event) {
        throw new IllegalStateException("You must override this method or IdentityProvider.authenticate");
    }
}
