package sara.won.quokka;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaSecurityRealmTest {

    @Test
    @Order(1)
    void shouldAccessPublicWhenAnonymous() {
        get("/api/public")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Order(2)
    void shouldNotAccessAdminWhenAnonymous() {
        get("/api/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    @Order(3)
    void shouldAccessAdminWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin123!")
                .when()
                .get("/api/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Order(4)
    void shouldNotAccessUserWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin123!")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @Order(5)
    void shouldAccessUserAndGetIdentityWhenUserAuthenticated() {
        given()
                .auth().preemptive().basic("user", "user123!")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("user"));
    }
}
