package at.riit;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserMeResourceTest {

    @Test
    void me() {
        given()
                .when().get("/user/me")
                .then()
                .statusCode(200);
    }

    @Test
    void details() {
        given()
                .when().get("/user/me/details")
                .then()
                .statusCode(200);
    }
}