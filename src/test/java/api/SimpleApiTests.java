package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import api.modelApi.fakeApiUsers.Address;
import api.modelApi.fakeApiUsers.Geolocation;
import api.modelApi.fakeApiUsers.Name;
import api.modelApi.fakeApiUsers.UserRoot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SimpleApiTests {

    /**
     * Вывод всех пользователей и проверка статус кода протокола
     * Плагин для преобразования JSON называется robo Pojo Generated
     */
    @Test
    public void getAllUsersTest(){
        given()
                .get("https://fakestoreapi.com/users")
                .then()
                .log().all()
                .statusCode(200);
    }

    /**
     * Проверка, что пользователь с таким id существует и имеет
     * правильную структуру зипкода
     */
    @Test
    public void getSingleUserTest(){
        int userId = 2;
        given()
                .pathParam("userId", userId)
                .get("https://fakestoreapi.com/users/{userId}")
                .then().log().all()
                .body("id", equalTo(userId)) //проверка поля ответа
                .body("address.zipcode", matchesPattern("^\\d{5}-\\d{4}$"))
                .statusCode(200);
    }

    /**
     * Проверка, что количество выведенных данных совпадает с указанным количеством
     */
    @Test
    public void getAllUsersWithLimitTest(){
        int limitSize = 5;
        given()
                .queryParam("limit", limitSize)
                .get("https://fakestoreapi.com/users")
                .then()
                .log().all()
                .statusCode(200)
                .body("", hasSize(limitSize));
    }

    /**
     * Проверка, что сортировка в параметрах работает
     */
    @Test
    public void getAllUsersSortByDescTest(){
        String sortType = "desc";
        //Записываем отсортированный вид ответа
        Response sortedResponce = given()
                .queryParam("sort", sortType)
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();

        //Записываем обычный вид ответа
        Response notSortedResponce = given()
                .get("https://fakestoreapi.com/users")
                .then().log().all()
                .extract().response();

        //записываются id всех отсортированных пользователей
        List<Integer> sortedResponceId = sortedResponce.jsonPath()
                                                        .getList("id");
        List<Integer> notSortedResponceId = notSortedResponce.jsonPath()
                                                        .getList("id");

        Assertions.assertNotEquals(sortedResponceId,notSortedResponceId);
        List<Integer> sortedByCode = notSortedResponceId.stream()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());

        Assertions.assertEquals(sortedByCode, sortedResponceId);
    }

    /**
     * Создание и отправка юзера
     */
    @Test
    public void addNewUserTest(){
        UserRoot bodyRequest = getUserRootTest();

        given().body(bodyRequest)
                .post("https://fakestoreapi.com/users")
                .then().log().all()
                .statusCode(200)
                .body("id", notNullValue());
    }

    /**
     * Проверка обновления данных системы
     */
    @Test
    public void updateUserTest(){
        UserRoot userRoot = getUserRootTest();
        String oldPassword = userRoot.getPassword();

        userRoot.setPassword("new123");

        given().body(userRoot)
                .put("https://fakestoreapi.com/users/" + userRoot.getId())
                .then().log().all()
                .body("password", not(equalTo(oldPassword)));
    }

    /**
     * Удаление пользователя
     * В реальных проектах не стоит забывать о том, что нужно после удаления
     * проверить удаление и попытку входа в систему, либо все, что связано
     * с удаленным пользователем
     */
    @Test
    public void deleteUserTest(){
        given().delete("https://fakestoreapi.com/users/7")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    public void getAuthTokenTest(){
        Map<String, String> userAuth = new HashMap<>();
        userAuth.put("username", "jimmie_k");
        userAuth.put("password", "klein*#%*");

        given().contentType(ContentType.JSON)
                .body(userAuth)
                .post("https://fakestoreapi.com/auth/login")
                .then().log().all()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();
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
