package tests.swaggerTests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import listener.CustomTpl;
import models.swagger.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.UserService;
import java.util.List;
import java.util.Random;
import static assertions.Conditions.*;

public class UserNewTests {

    private static Random random;
    private static UserService userService;
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        random = new Random();
        userService = new UserService();
    }

    private FullUser getRandomUser(){
        int randomNumber = Math.abs(random.nextInt());
        return FullUser.builder().login("Unhuman" + randomNumber).pass("Jumanji" + randomNumber)
                .build();
    }

    private FullUser getAdminUser(){
        return FullUser.builder().login("admin").pass("admin").build();
    }

    @Test
    public void positiveRegisterTest(){
        FullUser user = getRandomUser();
        userService.register(user).should(haseStatus(201))
                .should(haseMessage("User created"));
    }

    @Test
    public void negativeRegisterLoginUser(){
        FullUser user = getRandomUser();
        userService.register(user);//первая регистрация статус коды не проверяем
        userService.register(user).should(haseStatus(400))
                .should(haseMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPassword(){
        FullUser user = getRandomUser();
        user.setPass(null);
        userService.register(user)
                .should(haseStatus(400))
                .should(haseMessage("Missing login or password"));
    }

    @Test
    public void authAdmin(){
        FullUser user = getAdminUser();
        String token  = userService.auth(user).should(haseStatus(200))
                .should(haseJwt()).asJwt();
        Assertions.assertNotNull(token);//можно без этого
    }

    @Test
    public void positiveAuthNewUser(){
        FullUser user = getRandomUser();
        userService.register(user);
        userService.auth(user).should(haseStatus(200))
                .should(haseJwt());
    }

    @Test
    public void negativeAuth(){
        FullUser user = getRandomUser();
        userService.auth(user)
                .should(haseStatus(401))
                .should(haseError("Unauthorized"));
    }

    @Test
    public void positiveGetUserInfoTest(){
        FullUser user = getAdminUser();
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
        FullUser user = getRandomUser();
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
        FullUser user = getRandomUser();
        userService.register(user);
        String token = userService.auth(user).should(haseJwt()).asJwt();
        InfoWrapper infoWrapper = userService.deleteUser(token)
                .as(InfoWrapper.class);
        Assertions.assertEquals("User successfully deleted", infoWrapper.getInfo().getMessage());
    }

    @Test
    public void getAllLoginUser(){
        FullUser user = getRandomUser();
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
