package tests.Junit.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.fakeApiUser.Address;
import models.fakeApiUser.Geolocation;
import models.fakeApiUser.Name;
import models.fakeApiUser.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@Tag("smokes")
public class SimpleApiTests {

    @Test
    public void getAllUsers(){
        given().get("https://fakestoreapi.com/users")
            .then()
            .log().all().statusCode(200);
    }

    @Test
    public void getSingleUser(){
        int userId = 2;
        given().pathParam("userId", userId)
                .get("https://fakestoreapi.com/users/{userId}")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(userId))
                .body("address.zipcode", matchesPattern("\\d{5}-\\d{4}"));
    }

    @Test
    public void getAllUserWithLimit(){
        int limit = 3;
        given().queryParam("limit", limit)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .body("", hasSize(limit));
    }

    @Test
    public void getAllUsersSortByDesc(){
        String sortType = "desc";
        Response sortedResponse = given().queryParam("sort", sortType)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                        .extract().response();

        Response notSorted = given()
                .get("https://fakestoreapi.com/users")
                .then().log().all().extract().response();

        List<Integer> sortedResponseIds = sortedResponse.jsonPath().getList("id");
        List<Integer> notSortedResponseIds = notSorted.jsonPath().getList("id");

        List<Integer> sortedByCode = notSortedResponseIds
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        Assertions.assertNotEquals(sortedResponseIds, notSortedResponseIds);
        Assertions.assertEquals(sortedResponseIds, sortedByCode);
    }

    @Test
    public void addNewUser(){
        Name name = Name.builder().firstname("alex").lastname("yudin").build();
        Geolocation geolocation = new Geolocation("-31.00", "81.1231");
        Address address = Address.builder()
                .city("Moscow")
                .number(100)
                .street("Lenina")
                .zipcode("21231-4567")
                .geolocation(geolocation).build();
        UserRoot bodyRequest = UserRoot.builder()
                .name(name)
                .phone("89997546767").email("fake@mail.ru")
                .address(address)
                .password("34566")
                .username("testerwow").build();

        given().body(bodyRequest)
                .post("https://fakestoreapi.com/users")
                .then().log().all()
                .statusCode(200)
                .body("id", notNullValue());

    }

    private UserRoot getTestUser(){
        Name name = Name.builder().firstname("alex").lastname("yudin").build();
        Geolocation geolocation = new Geolocation("-31.00", "81.1231");
        Address address = Address.builder()
                .city("Moscow")
                .number(100)
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
        given().body(user)
                .put("https://fakestoreapi.com/users/7")
                .then().log().all()
                .body("password", not(equalTo(oldPassword)));
    }

    @Test
    public void deleteUser(){
        given().delete("https://fakestoreapi.com/users/7")
                .then()
                .log()
                .all();
    }


    @Test
    public void authUser(){
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("username", "mor_2314");
        userAuth.put("password", "83r5^_");


        given().contentType(ContentType.JSON).body(userAuth).post("https://fakestoreapi.com/auth/login")
                .then()
                .log().all()
                .statusCode(200)
                .body("token", notNullValue());
    }
}
