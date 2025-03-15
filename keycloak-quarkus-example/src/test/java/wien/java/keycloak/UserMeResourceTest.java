package wien.java.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestHTTPEndpoint(UserMeResource.class)
@QuarkusTestResource(KeycloakResource.class)
class UserMeResourceTest {

    @Test
    @TestSecurity(user = "")
    void meAnon() {
        given().redirects().follow(false)
                .when().get("/")
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).header("Location", containsString("realms/javavienna"));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {})
    void meUser() {
        var body = given()
                .when().get("/")
                .then()
                .statusCode(200)
                .extract()
                .asPrettyString();
        assertThat(body, containsString("testUser"));
    }

    @Test
    @TestSecurity(user = "testAdmin", roles = {"admin"})
    void detailsAdmin() {
        var body =  given()
                .when().get("details")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        assertThat(body.get("roles"), containsInAnyOrder("admin"));
    }

    @Test
    @TestSecurity(user = "testUser", roles = {})
    void detailsUser() {
        given()
                .when().get("details")
                .then()
                .statusCode(403);
    }
}