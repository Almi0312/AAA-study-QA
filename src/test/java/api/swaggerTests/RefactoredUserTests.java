package api.swaggerTests;

import api.assertion.AssertableResponse;
import api.assertion.Conditions;
import api.assertion.GenericAssertableResponse;
import api.listener.AdminUser;
import api.listener.AdminUserResolver;
import api.listener.CustomTpl;
import api.modelApi.swagger.FullUser;
import api.modelApi.swagger.Info;
import api.modelApi.swagger.JWTAuthData;
import api.service.UserService;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.List;


import static api.assertion.Conditions.hasMessage;
import static api.assertion.Conditions.hasStatusCode;
import static api.utils.RandomTestData.*;

@ExtendWith(AdminUserResolver.class)
@DisplayName("Отредактированные Api тесты")
public class RefactoredUserTests {
    private static UserService userService;
    private FullUser user;

    @BeforeEach
    public void initTestUser(){
        user = createRandomUser();
    }
    @BeforeAll
    public static void setUp() {
        userService = new UserService();
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
    }


    @Test
    public void positiveAdminAuthTest(@AdminUser FullUser admin){
        String token = userService.auth(admin)
                .should(Conditions.hasStatusCode(200))
                .asJWT();

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest(){

        userService.register(user)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"));

        String token = userService.auth(user)
                .should(hasStatusCode(200))
                .asJWT();

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveRegisterWithGamesTest() {

        Response response = userService.register(user)
                .should(hasStatusCode(201))
                .should(hasMessage("User created"))
                .asResponse();

        Info info = response.jsonPath().getObject("info", Info.class);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(info.getMessage()).as("Сообщение об ошибке не верно")
                .isEqualTo("фейк меседж");
        softAssertions.assertThat(response.statusCode()).as("Статус код не 200")
                .isEqualTo(201);
        softAssertions.assertAll();
    }

    @Test
    public void negativeRegisterLoginExistTest() {
        userService.register(user);
        userService.register(user)
                .should(hasStatusCode(400))
                .should(hasMessage("Login already exist"));
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        user.setPass(null);

        userService.register(user)
                .should(hasStatusCode(400))
                .should(hasMessage("Missing login or password"));
      }

    @Test
    public void negativeAuthTest(){
        userService.auth(user)
                        .should(hasStatusCode(401));
    }

    @Test
    public void positiveGetUserInfoTest(){
        FullUser user = createAdminUser();

        String token = userService.auth(user)
                .should(hasStatusCode(200)).asJWT();

        Assertions.assertNotNull(token, "Нет никакого значения");

        userService.getUserInfo(token)
                .should(hasStatusCode(200));
    }

    @Test
    public void negativeGetUserInfoJWTest(){
        userService.getUserInfo("sdadsa")
                .should(hasStatusCode(401));
    }

    @Test
    public void negativeGetUserInfoWithoutJWTTest(){
        userService.getUserInfo("")
                .should(hasStatusCode(401));
    }

    @Test
    public void positiveUserChangePasswordTest(){
        FullUser user = createRandomUser();
        String oldPassword = user.getPass();
        userService.register(user);

        String token = userService.auth(user).asJWT();
        String updatePassValue = "newPassword";
        userService.updatePass(updatePassValue, token)
                .should(hasStatusCode(200))
                .should(hasMessage("User password successfully changed"));

        user.setPass(updatePassValue);
        token = userService.auth(user)
                .should(hasStatusCode(200)).asJWT();

        user = userService.getUserInfo(token)
                .as(FullUser.class);
        Assertions.assertNotNull(user);
        Assertions.assertNotEquals(oldPassword, user.getPass());
}

    @Test
    public void negativeAdminChangePassword(){
        FullUser user = createAdminUser();
        String updatePassValue = "newpassUpdated";

        String token = userService.auth(user).asJWT();

        userService.updatePass(updatePassValue, token)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant update base users"));
    }

    @Test
    public void negativeDeleteAdminTest(){
        FullUser user = createAdminUser();
        String token = userService.auth(user).asJWT();

        userService.deleteUser(token)
                .should(hasStatusCode(400))
                .should(hasMessage("Cant delete base users"));
    }

    @Test
    public void positiveGetAllUsersTest(){
        List<String> users = userService.getAllUsers().asList(String.class);
        Assertions.assertTrue(users.size() > 3);
    }
}
