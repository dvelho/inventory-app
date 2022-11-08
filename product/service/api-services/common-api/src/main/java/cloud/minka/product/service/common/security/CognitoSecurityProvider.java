package cloud.minka.product.service.common.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.amazon.lambda.http.LambdaAuthenticationRequest;
import io.quarkus.amazon.lambda.http.model.AwsProxyRequest;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class CognitoSecurityProvider implements LambdaIdentityProvider {


    @Override
    public SecurityIdentity authenticate(AwsProxyRequest event) {
        try {
            ObjectMapper mapper;
            mapper = JsonMapper.builder()
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
            String json = mapper.writeValueAsString(event);
            return getPrincipal(getJwtPayload(json), mapper);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    private String getJwtPayload(String jwt) {
        /**
         * Can't get the JWT from the request context, so we have to parse it from the event
         * Also regex does not work in native mode, so we have to do it manually
         * The Jwt is verified by AWS, so we don't need to do it here
         * */
        jwt = jwt.substring(jwt.indexOf("Bearer") + 7);
        jwt = jwt.substring(0, jwt.indexOf("\"") - 1).split("\\.")[1];
        return new String(Base64.getDecoder().decode(jwt));

    }

    private QuarkusSecurityIdentity getPrincipal(String jwtPayload, ObjectMapper mapper) throws IOException {
        JsonNode jsonNode;
        jsonNode = mapper.readTree(jwtPayload);
        String username = jsonNode.get("cognito:username").asText();
        String email = jsonNode.get("email").asText();
        String domain = jsonNode.get("custom:domain").asText();
        String tenantId = jsonNode.get("custom:tenantId").asText();
        Set<String> roles = new HashSet<>();
        jsonNode.get("cognito:groups").forEach(jnode -> roles.add(jnode.asText()));
        Principal principal = new QuarkusPrincipal(username);
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
        builder.setPrincipal(principal)
                .addRoles(roles)
                .addAttribute("email", email)
                .addAttribute("domain", domain)
                .addAttribute("tenantId", tenantId);
        return builder.build();
    }
}
