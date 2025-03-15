package wien.java.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

public class KeycloakResource implements QuarkusTestResourceLifecycleManager {

    KeycloakContainer keycloak;

    @Override
    public Map<String, String> start() {
        keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.1")
                .withAdminUsername("admin")
                .withAdminPassword("admin")
                .withRealmImportFile("/javavienna-realm.json");
        keycloak.start();

        return Map.of(
                "quarkus.oidc.auth-server-url", keycloak.getAuthServerUrl() + "/realms/javavienna",
                "quarkus.oidc.credentials.secret", "secret"
        );
    }

    @Override
    public void stop() {
        if (keycloak != null) {
            keycloak.stop();
        }
    }
}
