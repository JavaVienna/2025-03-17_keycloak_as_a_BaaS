package wien.java.keycloak.keycloak;

import io.quarkus.vertx.web.Route;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.FileSystemAccess;
import io.vertx.ext.web.handler.StaticHandler;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StaticRoute {
    @Route(path = "/*", methods = Route.HttpMethod.GET)
    void secureStatic(RoutingContext rc) {
        StaticHandler.create(FileSystemAccess.RELATIVE, "content/").handle(rc);
    }
}
