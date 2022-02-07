import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReqresTests {
    @BeforeAll
    static void setUP() {
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    @DisplayName("Регистрация нового пользователя")
    void successfulRegisteredTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"1234azsx\" }";
        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Создания нового пользователя")
    void successfulCreateTest() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("/api/user")
                .then()
                .statusCode(201)
                .extract().response();
        assertThat(response.path("name").toString()).isEqualTo("morpheus");
        assertThat(response.path("job").toString()).isEqualTo("leader");
    }

    @Test
    @DisplayName("Получения List<>")
    void successfulGetListTest() {
        given()
                .when()
                .get("/api/user")
                .then()
                .statusCode(200)
                .body("data.name[0]", is("cerulean"));
    }

    @Test
    @DisplayName("Обновления пользователя")
    void successfulUpdateUserTest() {
        /*
        "data": {
                "id": 2,
                "email": "janet.weaver@reqres.in",
                "first_name": "Janet",
                "last_name": "Weaver",
                "avatar": "https://reqres.in/img/faces/2-image.jpg"
                }
         */
        String data = "{ \"first_name\": \"notJanet\", \"job\": \"leader\" }";
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(data)
                .put("/api/users/2")
                .then()
                .statusCode(200)
                .extract().response();
        assertThat(response.path("job").toString().equals("leader"));
        assertThat(response.path("first_name").toString().equals("notJanet"));

    }
    @Test
    @DisplayName("Удаление пользователя")
    void successfulDeleteUserTest() {
        given()
                .when()
                .delete("/api/user")
                .then()
                .statusCode(204);
    }
}
