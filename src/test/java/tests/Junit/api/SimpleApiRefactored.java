package tests.Junit.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import listener.CustomTpl;
import models.fakeApiUser.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SimpleApiRefactored {
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "https://fakestoreapi.com";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
    }

    @Test
    public void getAllUsers(){
        given().get("/users")
                .then().statusCode(200);
    }

    @Test
    public void getSingleUser(){
        int userId = 2;
        UserRoot response = given().pathParam("userId", userId)
                .get("/users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(UserRoot.class);
//        Name name = given().pathParam("userId", userId) // получаем только name
//                .get("/users/{userId}")
//                .then()
//                .extract().jsonPath().getObject("name", Name.class);

        Assertions.assertEquals(userId, response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("\\d{5}-\\d{4}"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 20})
    public void getAllUserWithLimit(int limitSize){
        List<UserRoot> userRootList = given().queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                //.extract().jsonPath().getList("", UserRoot.class); // 2 способа
                        .extract().as(new TypeRef<List<UserRoot>>() {});
        Assertions.assertEquals(limitSize, userRootList.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 40})
    public void getAllUsersErrorParams(int limitSize){
        List<UserRoot> userRootList = given().queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                //.extract().jsonPath().getList("", UserRoot.class); // 2 способа
                .extract().as(new TypeRef<List<UserRoot>>() {});
        Assertions.assertNotEquals(limitSize, userRootList.size());
    }

    @Test
    public void getAllUsersSortByDesc(){
        String sortType = "desc";
        List<UserRoot> userSortedRootList = given().queryParam("sort", sortType)
                .get("/users")
                .then()
                .extract().as(new TypeRef<List<UserRoot>>() {});

        List<UserRoot> userRootListNotSorted = given()
                .get("/users")
                .then().extract().as(new TypeRef<List<UserRoot>>() {});

        List<Integer> sortedResponseIds = userSortedRootList.stream()
                .map(UserRoot::getId).collect(Collectors.toList());
        List<Integer> notSortedResponseIdsByCode = userRootListNotSorted.stream()
                .map(x->x.getId())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        Assertions.assertNotEquals(userSortedRootList, userRootListNotSorted);
        Assertions.assertEquals(sortedResponseIds, notSortedResponseIdsByCode);
    }

    @Test
    public void addNewUser(){
        UserRoot user = getTestUser();

        int userId = given().body(user)
                .post("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("id");
        Assertions.assertNotNull(userId);
    }

    private UserRoot getTestUser(){
        Random random = new Random();
        Name name = Name.builder().firstname("Thomas").lastname("Anderson").build();
        Geolocation geolocation = new Geolocation("-31.00", "81.1231");
        Address address = Address.builder()
                .city("Moscow")
                .number(random.nextInt(100))
                .street("Lenina")
                .zipcode("21231-4567")
                .geolocation(geolocation).build();
        return UserRoot.builder()
                .name(name)
                .phone("89997546767").email("fake@mail.ru")
                .address(address)
                .password("34566")
                .username("testerwow").build();
    }

    @Test
    public void updateUser(){
        UserRoot user = getTestUser();
        String oldPassword = user.getPassword();
        user.setPassword("231235");
        UserRoot userUpdated = given().body(user)
                .pathParam("userId", user.getId())
                .put("/users/{userId}" )
                .then().extract()
                .as(UserRoot.class);
        Assertions.assertNotEquals(oldPassword, userUpdated.getPassword());
    }

    @Test
    public void authUser() {
        AuthData authData = new AuthData("mor_2314", "83r5^_");

        String token = given()
                .contentType(ContentType.JSON)
                .body(authData)
                .post("/auth/login")
                .then()
                .log().all() // Логирование запроса и ответа
                .statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }
}
