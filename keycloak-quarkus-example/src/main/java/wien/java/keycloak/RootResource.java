package wien.java.keycloak;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class RootResource {

    @Produces(MediaType.TEXT_HTML)
    @GET
    public Response root() {
        var sb = new StringBuilder("<html><body>");
        sb.append("<a href=\"./echo\">Echo Request</a><br>");
        sb.append("<a href=\"./users/me\">User Simple (non-admin)</a><br>");
        sb.append("<a href=\"./users/me/details\">User Admin (admin)</a><br>");
        sb.append("<a href=\"./tokens\">Token</a><br>");
        sb.append("<br>");
        sb.append("<a href=\"./q/swagger-ui/\">Swagger UI</a><br>");
        return Response.ok(sb.toString()).build();
    }

}
