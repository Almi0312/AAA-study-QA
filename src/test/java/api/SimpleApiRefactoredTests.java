package api;

import api.listener.CustomTpl;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import api.modelApi.fakeApiUsers.Address;
import api.modelApi.fakeApiUsers.Geolocation;
import api.modelApi.fakeApiUsers.Name;
import api.modelApi.fakeApiUsers.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class SimpleApiRefactoredTests {
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "https://fakestoreapi.com";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());

    }

    @Test
    public void getAllUsersTest(){
        given()
                .get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    public void getSingleUserTest(){
        int userId = 5;
        UserRoot response = given()
                .pathParam("userId", userId)
                .get("users/{userId}")
                .then()
                .statusCode(200)
                .extract().as(UserRoot.class);
        //Достаем поле из JSON файла класса UserRoot
        Name name = given()
                .pathParam("userId", userId)
                .get("users/{userId}")
                .then()
                .statusCode(200)
                .extract().jsonPath().getObject("name", Name.class);
        Assertions.assertEquals(userId,response.getId());
        Assertions.assertTrue(response.getAddress().getZipcode().matches("^\\d{5}-\\d{4}$"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 20})
    public void getAllUsersWithLimitTest(int limitSize){
                List<UserRoot> users = given()
                .queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<>() {
                });
        Assertions.assertEquals(limitSize, users.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 40})
    public void getAllUsersWithLimitErrorParams(int limitSize){
        List<UserRoot> users = given()
                .queryParam("limit", limitSize)
                .get("/users")
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<List<UserRoot>>() {});
        Assertions.assertNotEquals(limitSize, users.size());
    }

    @Test
    public void getAllUsersSortByDescTest(){
        String sortType = "desc";
        //Записываем отсортированный вид ответа
        List<UserRoot> sortedResponcesUsers = given()
                .queryParam("sort", sortType)
                .get("/users")
                .then()
                .extract().jsonPath().getList("", UserRoot.class);

        //Записываем обычный вид ответа
        List<UserRoot> notSortedResponceUsers = given()
                .get("/users")
                .then().log().all()
                .extract().jsonPath().getList("", UserRoot.class);

        Assertions.assertNotEquals(sortedResponcesUsers,notSortedResponceUsers);

        //записываются id всех отсортированных пользователей
        List<Integer> sortedResponceId = sortedResponcesUsers.stream()
                .map(x -> x.getId()).collect(Collectors.toList());

        List<Integer> sortedByCode = notSortedResponceUsers.stream()
                .map(UserRoot::getId)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Assertions.assertEquals(sortedByCode, sortedResponceId);
    }

    @Test
    public void addNewUserTest(){
        UserRoot bodyRequest = getUserRootTest();

        Integer userId = given().body(bodyRequest)
                .post("/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getInt("id");
//                .body("id", notNullValue());

        Assertions.assertNotNull(userId);
    }

    @Test
    public void updateUserTest(){
        UserRoot userRoot = getUserRootTest();
        String oldPassword = userRoot.getPassword();

        userRoot.setPassword("new123");

         UserRoot updateRoot = given().body(userRoot)
                 .pathParam("userId", userRoot.getId())
                .put("/users/{userId}")
                .then()
                .extract().as(UserRoot.class);
//                .body("password", not(equalTo(oldPassword)));

        Assertions.assertNotEquals(updateRoot.getPassword(), oldPassword);
    }


    private UserRoot getUserRootTest(){
        Name name = new Name("Thomas", "Anderson");
        Geolocation geolocation = new Geolocation("-31.212","81.2312");

        Address address = Address.builder()
                .city("Moscow")
                .number(100)
                .street("Sezam 13")
                .zipcode("12132-1231")
                .geolocation(geolocation).build();

        return UserRoot.builder()
                .name(name)
                .address(address)
                .phone("89874355321")
                .username("ThomasAdmin")
                .password("1234").build();
    }
}
