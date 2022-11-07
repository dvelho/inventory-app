package cloud.minka.product.service.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.amazon.lambda.http.LambdaAuthenticationRequest;
import io.quarkus.amazon.lambda.http.model.AwsProxyRequest;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.impl.jose.JWT;

import javax.enterprise.context.ApplicationScoped;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped

public class CognitoSecurityProvider implements LambdaIdentityProvider {

    JWT jwt;

    @Override
    public SecurityIdentity authenticate(AwsProxyRequest event) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(event);
            System.out.println("event: " + json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println("Decoding:");

        jwt = new JWT();
        JsonObject a = jwt.decode(event.getMultiValueHeaders().get("Authorization").get(0).replace("Bearer ", ""));

        System.out.println("event: " + a);
        System.out.println("event: " + a.getString("cognito:username"));
        System.out.println("event: " + a.getString("cognito:groups"));
        System.out.println("event: " + a.getString("custom:domain"));
        System.out.println("event: " + a.getString("custom:custom:tenantId"));

        Principal principal = new QuarkusPrincipal(event.getRequestContext().getAuthorizer().getClaims().getEmail());
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
        builder.setPrincipal(principal).addRoles(Arrays.stream(a.getString("cognito:groups").split(",")).collect(Collectors.toSet()));
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
