package cloud.minka.product.service.common.security;

import io.quarkus.amazon.lambda.http.LambdaAuthenticationRequest;
import io.quarkus.amazon.lambda.http.model.AwsProxyRequest;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.security.Principal;
import java.util.Optional;

@ApplicationScoped

public class CognitoSecurityProvider implements LambdaIdentityProvider {


    @Override
    public SecurityIdentity authenticate(AwsProxyRequest event) {
        System.out.println(event);
        // if (event.getMultiValueHeaders() == null || !event.getMultiValueHeaders().containsKey("x-user"))
        //     return null;
        Principal principal = new QuarkusPrincipal(event.getMultiValueHeaders().getFirst("x-user"));
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
        builder.setPrincipal(principal);
        return builder.build();

    }

    @Override
    public Class<LambdaAuthenticationRequest> getRequestType() {
        return LambdaAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(LambdaAuthenticationRequest request, AuthenticationRequestContext context) {
        AwsProxyRequest event = request.getEvent();
        SecurityIdentity identity = authenticate(event);
        if (identity == null) {
            return Uni.createFrom().optional(Optional.empty());
        }
        return Uni.createFrom().item(identity);
    }
}
