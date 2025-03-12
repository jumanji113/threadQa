package tests.swaggerTests;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import listener.CustomTpl;
import models.fakeApiUser.UserRoot;
import models.swagger.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserTests {

    private static Random random;
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
    }

    @Test
    public void positiveRegisterTest(){
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = new FullUser().builder().login("Unhuman" + randomNumber).pass("Jumanji")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(info.getMessage(), "User created");
    }

    @Test
    public void negativeRegisterLoginUser(){
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = new FullUser().builder().login("Unhuman" + randomNumber).pass("Jumanji")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(info.getMessage(), "User created");

        Info errorInfo = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(errorInfo.getMessage(), "Login already exist");
    }

    @Test
    public void authAdmin(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveAuthNewUser(){
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = new FullUser().builder().login("Unhuman" + randomNumber).pass("Jumanji")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(info.getMessage(), "User created");

        JwtAuthData jwtAuthData = new JwtAuthData(user.getPass(), user.getLogin());
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);
    }

    @Test
    public void negativeAuth(){
        JwtAuthData jwtAuthData = new JwtAuthData("dsadasdas", "randomPAS");
        ErrorAuth errorAuth = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(401)
                .extract().as(ErrorAuth.class); // Используем as() для маппинга
        Assertions.assertEquals("Unauthorized", errorAuth.getError());
    }

    @Test
    public void positiveGetUserInfoTest(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        given()
                .auth().oauth2(token)//header("Authorization", "Bearer " + token) - можно так
                .get("/api/user")
                .then()
                .statusCode(200);
    }

    @Test
    public void negativeGetUserInfoTest(){
        given()
                .auth().oauth2("some token")
                .get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    public void negativeGetUserWithoutJwtTest(){
        given()
                .get("/api/user")
                .then()
                .statusCode(401);
    }

    @Test
    public void positiveUpdatePasswordUser(){
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = new FullUser().builder().login("Unhuman" + randomNumber).pass("Jumanji")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(info.getMessage(), "User created");

        String updatedPass = "newJumanji113";
        Map<String, String> newPass = new HashMap<>();
        newPass.put("password", updatedPass);

        JwtAuthData jwtAuthData = new JwtAuthData(user.getPass(), user.getLogin());
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        InfoWrapper responsePassInfo = given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(newPass)
                .put("/api/user")
                .then().extract().as(InfoWrapper.class);

        Assertions.assertEquals("User password successfully changed", responsePassInfo.getInfo().getMessage());

        jwtAuthData.setPassword(updatedPass);
        token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token);

        FullUser fullUserUpdated = given()
                .auth().oauth2(token)//header("Authorization", "Bearer " + token) - можно так
                .get("/api/user")
                .then()
                .statusCode(200).extract().as(FullUser.class);
        Assertions.assertEquals(updatedPass, fullUserUpdated.getPass());
    }

    @Test
    public void negativeUpdatePasswordUser(){
        String updatedPass = "newJumanji113";
        Map<String, String> newPass = new HashMap<>();
        newPass.put("password", updatedPass);

        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        InfoWrapper responsePassInfo = given()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(newPass)
                .put("/api/user")
                .then().statusCode(400).extract().as(InfoWrapper.class);

        Assertions.assertEquals("Cant update base users", responsePassInfo.getInfo().getMessage());
    }

    @Test
    public void negativeDeleteUser(){
        JwtAuthData jwtAuthData = new JwtAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        InfoWrapper infoWrapper = given().auth().oauth2(token)
                .delete("/api/user")
                .then().extract().as(InfoWrapper.class);
        Assertions.assertEquals("Cant delete base users", infoWrapper.getInfo().getMessage());
    }

    @Test
    public void positiveDeleteUser(){
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = new FullUser().builder().login("Unhuman" + randomNumber).pass("Jumanji")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(info.getMessage(), "User created");

        JwtAuthData jwtAuthData = new JwtAuthData(user.getPass(), user.getLogin());

        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");
        InfoWrapper infoWrapper = given().auth().oauth2(token)
                .delete("/api/user")
                .then().extract().as(InfoWrapper.class);
        Assertions.assertEquals("User successfully deleted", infoWrapper.getInfo().getMessage());
    }

    @Test
    public void getAllLoginUser(){

        int randomNumber = Math.abs(random.nextInt());
        FullUser user = new FullUser().builder().login("Unhuman" + randomNumber).pass("Jumanji")
                .build();
        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then()
                .statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(info.getMessage(), "User created");

        JwtAuthData jwtAuthData = new JwtAuthData(user.getPass(), user.getLogin());
        String token = given().contentType(ContentType.JSON)
                .body(jwtAuthData)
                .post("/api/login")
                .then()
                .statusCode(200)
                .extract().jsonPath().getString("token");

        FullUser fullUser = given()
                .auth().oauth2(token)//header("Authorization", "Bearer " + token) - можно так
                .get("/api/user")
                .then()
                .statusCode(200).extract().as(FullUser.class);

        List<String> users = given().get("/api/users")
                .then()
                .statusCode(200).extract().as(new TypeRef<List<String>>() {});
        Assertions.assertTrue(users.contains(fullUser.getLogin()));
    }

    @Test
    public void getAllLoginUserCheckList(){
        List<String> users = given().get("/api/users")
                .then()
                .statusCode(200).extract().as(new TypeRef<List<String>>() {});
        Assertions.assertTrue(users.size() > 3);
    }

}
