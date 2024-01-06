package api.swaggerTests;

import api.listener.CustomTpl;
import api.modelApi.swagger.FullUser;
import api.modelApi.swagger.Info;
import api.modelApi.swagger.JWTAuthData;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserTests {
    private static Random random;

    @BeforeAll
    public static void setUp() {
        random = new Random();
        RestAssured.baseURI = "http://85.192.34.140:8080";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
    }

    @Test
    public void positiveAdminAuthTest(){
        JWTAuthData auth= new JWTAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveNewUserAuthTest(){
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JWTAuthData auth= new JWTAuthData(user.getLogin(), user.getPass());

        String token = given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");

        Assertions.assertNotNull(token);
    }

    @Test
    public void positiveRegisterTest() {
        int randomNumber = Math.abs(random.nextInt());
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());
    }

    @Test
    public void negativeRegisterLoginExistTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        Info errorInfo = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Login already exist", errorInfo.getMessage());
    }

    @Test
    public void negativeRegisterNoPasswordTest() {
        int randomNumber = Math.abs(random.nextInt());

        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("Missing login or password", info.getMessage());
    }

    @Test
    public void negativeAuthTest(){
        JWTAuthData auth= new JWTAuthData("sadas", "wdsad");

        given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(401);
    }

    @Test
    public void positiveGetUserInfo(){
        JWTAuthData auth= new JWTAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token, "Нет никакого значения");
        given().auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200);
    }

    @Test
    public void negativeGetUserInfoJWTest(){
        given().auth().oauth2("dsad")
                .get("/api/user")
                .then().statusCode(401);
    }

    @Test
    public void negativeGetUserInfoWithoutJWTTest(){
        given()
                .get("/api/user")
                .then().statusCode(401);
    }

    @Test
    public void positiveUserChangePassword(){
        int randomNumber = Math.abs(random.nextInt());
        String updatePassValue = "newpassUpdated";
        FullUser user = FullUser.builder()
                .login("threadQATestUser" + randomNumber)
                .pass("passwordCOOL")
                .build();

        Info info = given().contentType(ContentType.JSON)
                .body(user)
                .post("/api/signup")
                .then().statusCode(201)
                .extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals("User created", info.getMessage());

        JWTAuthData auth= new JWTAuthData(user.getLogin(), user.getPass());
        String token = given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token, "Нет никакого значения");

        Map<String, String> password = new HashMap<>();
        password.put("password", updatePassValue);

        Info info1 = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then().statusCode(200)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("success", info1.getStatus());
        Assertions.assertEquals("User password successfully changed", info1.getMessage());

        auth.setPassword(updatePassValue);
        token = given().contentType(ContentType.JSON)
                        .body(auth).post("/api/login")
                        .then().statusCode(200)
                        .extract().jsonPath().getString("token");

        FullUser userUpdated = given().auth().oauth2(token)
                .get("/api/user")
                .then().statusCode(200)
                .extract().as(FullUser.class);

        Assertions.assertNotEquals(user.getPass(), userUpdated.getPass());
    }

    @Test
    public void negativeAdminChangePassword(){
        String updatePassValue = "newpassUpdated";

        JWTAuthData auth= new JWTAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token, "Нет никакого значения");

        Map<String, String> password = new HashMap<>();
        password.put("password", updatePassValue);

        Info info1 = given().contentType(ContentType.JSON)
                .auth().oauth2(token)
                .body(password)
                .put("/api/user")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("fail", info1.getStatus());
        Assertions.assertEquals("Cant update base users", info1.getMessage());
    }

    @Test
    public void negativeDeleteAdmin(){
        JWTAuthData auth= new JWTAuthData("admin", "admin");
        String token = given().contentType(ContentType.JSON)
                .body(auth)
                .post("/api/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
        Assertions.assertNotNull(token, "Нет никакого значения");

        Info info = given().auth().oauth2(token)
                .delete("/api/user")
                .then().statusCode(400)
                .extract().jsonPath().getObject("info", Info.class);

        Assertions.assertEquals("fail", info.getStatus());
    }




}