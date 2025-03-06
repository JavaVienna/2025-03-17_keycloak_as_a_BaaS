package at.riit;

import com.fasterxml.jackson.databind.util.ClassUtil;
import io.quarkus.security.Authenticated;
import io.quarkus.security.credential.Credential;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.NoCache;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Path("users/me")
public class UserMeResource {

    @Inject
    SecurityIdentity securityIdentity;

    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Authenticated
    @NoCache
    public Response me() {
        return Response.ok(new User(securityIdentity)).build();
    }

    @Path("details")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @RolesAllowed("admin")
    @NoCache
    public Response details() {
        return Response.ok(new DetailedUser(securityIdentity)).build();
    }

    public static class User {

        final String userName;

        User(SecurityIdentity securityIdentity) {
            userName = securityIdentity.getPrincipal().getName();
        }

        public String getUserName() {
            return userName;
        }
    }

    public static class DetailedUser extends User {
        final Map<String, Object> attributes;
        private final Set<String> roles;
        private final Set<Credential> credentials;

        DetailedUser(SecurityIdentity securityIdentity) {
            super(securityIdentity);
            this.attributes = securityIdentity.getAttributes();
            this.roles = securityIdentity.getRoles();
            this.credentials = securityIdentity.getCredentials();
        }

        public Map<String, String> getAttributes(){
            return attributes.entrySet().stream()
                    .filter(e -> isPrintable(e.getValue().getClass()))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
        }

        public Set<String> getRoles(){
            return roles;
        }

        public Set<String> getCredentials() {
            return credentials.stream().map(c -> c.getClass().getName()).collect(Collectors.toSet());
        }

        private static boolean isPrintable(Class<?> clazz) {
            return clazz.isPrimitive() ||
                    clazz == Integer.class ||
                    clazz == Double.class ||
                    clazz == Boolean.class ||
                    clazz == Byte.class ||
                    clazz == Character.class ||
                    clazz == Float.class ||
                    clazz == Long.class ||
                    clazz == Short.class ||
                    clazz == String.class;
        }
    }
}
