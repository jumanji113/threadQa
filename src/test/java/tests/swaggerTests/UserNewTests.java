package tests.swaggerTests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import listener.AdminUser;
import listener.AdminUserResolver;
import listener.CustomTpl;
import models.swagger.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import services.UserService;
import utils.RandomTestData;

import java.util.List;
import java.util.Random;
import static assertions.Conditions.*;
import static utils.RandomTestData.*;

@ExtendWith(AdminUserResolver.class)
public class UserNewTests {

    private static Random random;
    private static UserService userService;
    private static RandomTestData randomTestData;
    private FullUser user;

    @BeforeEach
    public void initUser(){
        user = getRandomUser();
    }

    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
        userService = new UserService();
        randomTestData = new RandomTestData();
    }

    @Test
    public void positiveRegisterTestBase(){
        userService.register(user)
                .should(haseStatus(201))
                .should(haseMessage("User created"));
    }

    @Test
    public void positiveRegisterTest(){
        Response response = userService.register(user)
                //.should(haseStatus(201))
                //.should(haseMessage("User created"))
        .asResponse();

        Info info = response.jsonPath().getObject("info", Info.class);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(info.getMessage()).as("Сообщение об ошибке было неверное ")
                .isEqualTo("fake message");
        softAssertions.assertThat(response.statusCode()).as("Статус код не был двухсотым")
                .isEqualTo(201);
        softAssertions.assertAll();

    }

    @Test
    public void positiveRegisterTestWithService(){
        FullUser user = getRandomUserWithGames();
        userService.register(user).should(haseStatus(201))
                .should(haseMessage("User created"));
    }

    @Test
    public void negativeRegisterLoginUser(){
        userService.register(user);//первая регистрация статус коды не проверяем
        userService.register(user).should(haseStatus(400))
                .should(haseMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPassword(){
        user.setPass(null);
        userService.register(user)
                .should(haseStatus(400))
                .should(haseMessage("Missing login or password"));
    }

    @Test
    public void authAdmin(@AdminUser FullUser admin){
        String token  = userService.auth(admin).should(haseStatus(200))
                .should(haseJwt()).asJwt();
        Assertions.assertNotNull(token);//можно без этого
    }

    @Test
    public void positiveAuthNewUser(){
        userService.register(user);
        userService.auth(user).should(haseStatus(200))
                .should(haseJwt());
    }

    @Test
    public void negativeAuth(){
        userService.auth(user)
                .should(haseStatus(401))
                .should(haseError("Unauthorized"));
    }

    @Test
    public void positiveGetUserInfoTest(){
        String token  = userService.auth(user).should(haseStatus(200))
                .should(haseJwt()).asJwt();
        userService.getUserInfo(token)
                        .should(haseStatus(200));
    }

    @Test
    public void negativeGetUserInfoTest(){
        userService.getUserInfo("someTOKEN")
                        .should(haseStatus(401));
    }

    @Test
    public void negativeGetUserWithoutJwtTest(){
        userService.getUserInfo()
                        .should(haseStatus(401));
    }

    @Test
    public void positiveUpdatePasswordUser(){
        userService.register(user);

        String token = userService.auth(user).asJwt();
        String updatedPass = "newJumanji113";
        userService.updatePass(updatedPass, token)
                .should(haseStatus(200))
                .should(haseMessage("User password successfully changed"));
        user.setPass(updatedPass);
        token = userService.auth(user).asJwt();
        Assertions.assertNotNull(token);

        FullUser fullUserUpdated = userService.getUserInfo(token).as(FullUser.class);
        Assertions.assertEquals(updatedPass, fullUserUpdated.getPass());
    }

    @Test
    public void negativeUpdatePasswordUser(){
        String updatedPass = "newJumanji113";
        FullUser user = getAdminUser();
        String token  = userService.auth(user).should(haseStatus(200))
                .should(haseJwt()).asJwt();
        InfoWrapper responsePassInfo = userService.updatePass(updatedPass, token)
                .should(haseStatus(400)).as(InfoWrapper.class);
        Assertions.assertEquals("Cant update base users", responsePassInfo.getInfo().getMessage());
    }

    @Test
    public void negativeDeleteUser(){
        FullUser user = getAdminUser();
        String token  = userService.auth(user).should(haseStatus(200))
                .should(haseJwt()).asJwt();
        InfoWrapper infoWrapper = userService.deleteUser(token)
                .as(InfoWrapper.class);
        Assertions.assertEquals("Cant delete base users", infoWrapper.getInfo().getMessage());
    }

    @Test
    public void positiveDeleteUser(){
        userService.register(user);
        String token = userService.auth(user).should(haseJwt()).asJwt();
        InfoWrapper infoWrapper = userService.deleteUser(token)
                .as(InfoWrapper.class);
        Assertions.assertEquals("User successfully deleted", infoWrapper.getInfo().getMessage());
    }

    @Test
    public void getAllLoginUser(){
        userService.register(user);
        String token = userService.auth(user).should(haseJwt()).asJwt();
        List<String> users = userService.getAllUsers()
                .should(haseStatus(200)).asList(String.class);
        Assertions.assertTrue(users.contains(user.getLogin()));
    }

    @Test
    public void getAllLoginUserCheckList(){
        List<String> users = userService.getAllUsers()
                .should(haseStatus(200)).asList(String.class);
        Assertions.assertTrue(users.size() > 3);
    }

}
