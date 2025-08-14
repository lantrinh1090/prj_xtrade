package Testcase;

import Model.HistoryDP;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://xtrade-ai.hoadev.io.vn/auth";  // Thay bằng URL thật
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void testLoginWithServer() {
        given()
                .contentType("application/json")
                .body("{ \"username\": \"valid_user\", \"password\": \"correct_pass\", \"server\": \"serverA\" }")
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = HistoryDP.class)
    public void testLoginInvalidCombinations(String username, String password, String server) {
        given()
                .contentType("application/json")
                .body("{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"server\": \"" + server + "\" }")
                .when()
                .post("/login")
                .then()
                .statusCode(anyOf(equalTo(401), equalTo(400), equalTo(422)))
                .body("error", notNullValue());
    }


}
