package at.riit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TokenResourceTest {

    @Test
    void getTokens() {
        given()
                .when().get("/tokens")
                .then()
                .statusCode(200)
                .and()
                ;
    }
}