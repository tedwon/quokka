package sara.won.quokka;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
public class MemosEndpointTest {

//    @BeforeEach
//    void init() {
//        // Delete test data
//        try {
//            final int idSara = (int) ((ArrayList<?>) given()
//                    .when().get("/quokka/api/v1/memo/search/Meet Sara at school at 2:45").getBody().jsonPath().get("id")).get(0);
//            // Delete
//            given()
//                    .when().delete("/quokka/api/v1/memo/" + idSara)
//                    .then()
//                    .statusCode(204);
//
//            final int idNara = (int) ((ArrayList<?>) given()
//                    .when().get("/quokka/api/v1/memo/search/Meet Nara at school at 2:45").getBody().jsonPath().get("id")).get(0);
//            given()
//                    .when().delete("/quokka/api/v1/memo/" + idNara)
//                    .then()
//                    .statusCode(204);
//
//            final int idSaraNara = (int) ((ArrayList<?>) given()
//                    .when().get("/quokka/api/v1/memo/search/Meet Sara and Katie at school at 2:45").getBody().jsonPath().get("id")).get(0);
//            given()
//                    .when().delete("/quokka/api/v1/memo/" + idSaraNara)
//                    .then()
//                    .statusCode(204);
//        } catch (Exception e) {
//        }
//    }
//
//    @Test
//    public void testListAllMemos() {
//        // Create
//        given()
//                .body("{\n" +
//                        "    \"title\": \"Pick up Sara\",\n" +
//                        "    \"body\": \"Meet Sara at school at 2:45\",\n" +
//                        "    \"tags\": \"wess\",\n" +
//                        "    \"pin\": true\n" +
//                        "}")
//                .contentType("application/json")
//                .when()
//                .post("/quokka/api/v1/memo")
//                .then()
//                .statusCode(201)
//                .body("title", containsString("Pick up Sara"));
//
//        given()
//                .body("{\n" +
//                        "    \"title\": \"Pick up Nara\",\n" +
//                        "    \"body\": \"Meet Nara at school at 2:45\",\n" +
//                        "    \"tags\": \"tafe\",\n" +
//                        "    \"pin\": true\n" +
//                        "}")
//                .contentType("application/json")
//                .when()
//                .post("/quokka/api/v1/memo")
//                .then()
//                .statusCode(201);
//
//        given()
//                .when()
//                .body("{\n" +
//                        "    \"title\": \"Pick up Sara\",\n" +
//                        "    \"body\": \"Meet Sara at school at 2:45\",\n" +
//                        "    \"tags\": \"wess\",\n" +
//                        "    \"pin\": true\n" +
//                        "}")
//                .contentType("application/json")
//                .post("/quokka/api/v1/memo")
//                .then()
//                .statusCode(409);
//
//        // Retrieve all data
//        given()
//                .when().get("/quokka/api/v1/memo")
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString("Pick up Sara"),
//                        containsString("Meet Sara at school at 2:45"),
//                        containsString("wess"),
//                        containsString("true")
//                );
//
//        // Search by keyword
//        given()
//                .when().get("/quokka/api/v1/memo/search/Meet Sara at school at 2:45")
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString("Pick up Sara"),
//                        containsString("Meet Sara at school at 2:45"),
//                        containsString("wess"),
//                        containsString("true")
//                );
//
//        // Search & Update
//        final int idSara = (int) ((ArrayList<?>) given()
//                .when().get("/quokka/api/v1/memo/search/Meet Sara at school at 2:45").getBody().jsonPath().get("id")).get(0);
//        given()
//                .when()
//                .body("{\n" +
//                        "    \"title\": \"Pick up Sara and Katie\",\n" +
//                        "    \"body\": \"Meet Sara and Katie at school at 2:45\",\n" +
//                        "    \"tags\": \"wess,bshs\",\n" +
//                        "    \"pin\": true\n" +
//                        "}")
//                .contentType("application/json")
//                .put("/quokka/api/v1/memo/" + idSara)
//                .then()
//                .statusCode(200);
//
//        final int idNara = (int) ((ArrayList<?>) given()
//                .when().get("/quokka/api/v1/memo/search/Meet Nara at school at 2:45").getBody().jsonPath().get("id")).get(0);
//        given()
//                .when()
//                .body("{\n" +
//                        "    \"title\": \"Pick up Nara\",\n" +
//                        "    \"body\": \"Meet Sara and Katie at school at 2:45\",\n" +
//                        "    \"tags\": \"tafe\",\n" +
//                        "    \"pin\": true\n" +
//                        "}")
//                .contentType("application/json")
//                .put("/quokka/api/v1/memo/" + idNara)
//                .then()
//                .statusCode(409);
//
//        // Retrieve
//        given()
//                .when().get("/quokka/api/v1/memo")
//                .then()
//                .statusCode(200)
//                .body(
//                        containsString("Pick up Sara and Katie"),
//                        containsString("Meet Sara and Katie at school at 2:45"),
//                        containsString("wess"),
//                        containsString("Pick up Nara"),
//                        containsString("Meet Nara at school at 2:45"),
//                        containsString("tafe"),
//                        containsString("true")
//                );
//
//        // Delete
//        given()
//                .when().delete("/quokka/api/v1/memo/" + idSara)
//                .then()
//                .statusCode(204);
//
//        given()
//                .when().delete("/quokka/api/v1/memo/" + idNara)
//                .then()
//                .statusCode(204);
//
//        // Retrieve
//        given()
//                .when().get("/quokka/api/v1/memo")
//                .then()
//                .statusCode(200)
//                .body(
//                        not(containsString("Pick up Nara")),
//                        not(containsString("Meet Nara at school at 2:45")),
//                        not(containsString("tafe"))
//                );
//    }

}
