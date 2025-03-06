package at.riit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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