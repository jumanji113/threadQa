package assertions;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.swagger.JwtAuthData;

import java.util.List;

@RequiredArgsConstructor
public class AssertableResponse {
    private final ValidatableResponse response;

    public AssertableResponse should(Condition condition){
        condition.check(response);
        return this;
    }

    public String asJwt(){
        return response.extract().jsonPath().getString("token");
    }

    public <T> T as (Class<T> tClass){
        return response.extract().as(tClass);
    }

    public <T> T as (String jsonPath, Class<T> tClass){
        return response.extract().jsonPath().getObject(jsonPath, tClass);
    }

    public <T> List<T> asList(){
        return response.extract().as(new TypeRef<List<T>>() {});
    }

    public <T> List<T> asList(String jsonPath, Class<T> tClass){
        return response.extract().jsonPath().getList(jsonPath, tClass);
    }

    public <T> List<T> asList(Class<T> tClass){
        return response.extract().jsonPath().getList("", tClass);
    }

    public Response asResponse(){
        return response.extract().response();
    }
}
