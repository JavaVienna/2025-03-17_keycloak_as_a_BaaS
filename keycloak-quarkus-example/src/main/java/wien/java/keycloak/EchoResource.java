package wien.java.keycloak;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/echo")
public class EchoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(@Context HttpHeaders headers) {
        Map<String,String> headerMap = new HashMap<>();

        headers.getRequestHeaders().forEach((key,values)->
                headerMap.put(key, String.join(", ", values)));

        return Response.ok(headerMap).build();
    }
}
