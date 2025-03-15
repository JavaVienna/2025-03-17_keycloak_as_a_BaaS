package wien.java.keycloak;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class EchoResourceTest {
    @Test
    void testEchoEndpoint() {
        given()
          .when().get("/echo")
          .then()
             .statusCode(200);
    }

}