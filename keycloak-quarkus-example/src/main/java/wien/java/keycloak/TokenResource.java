package wien.java.keycloak;

import io.quarkus.logging.Log;
import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import io.quarkus.security.Authenticated;
import io.smallrye.jwt.auth.cdi.NullJsonWebToken;
import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/tokens")
public class TokenResource {

    /**
     * Injection point for the ID token issued by the OpenID Connect provider
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

    /**
     * Injection point for the access token issued by the OpenID Connect provider
     */
    @Inject
    JsonWebToken accessToken;

    /**
     * Injection point for the refresh token issued by the OpenID Connect provider
     */
    @Inject
    RefreshToken refreshToken;

    /**
     * Returns the tokens available to the application.
     * This endpoint exists only for demonstration purposes.
     * Do not expose these tokens in a real application.
     *
     * @return an HTML page containing the tokens available to the application.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getTokens() {
        JsonWebToken decodedRefreshToken = new NullJsonWebToken();
        try {
            var jwtParser = new DefaultJWTParser();
            var t = jwtParser.parseOnly(refreshToken.getToken());
            if (t != null) {
                decodedRefreshToken = t;
            }
        } catch (Exception e){
            Log.error(e);
        }

        Map<String, JsonWebToken> tokens = Map.of(
            "IDToken", idToken ,
            "AccessToken", accessToken,
            "RefreshToken", decodedRefreshToken
                )
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getName() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Response.ok(tokens).build();
    }
}
