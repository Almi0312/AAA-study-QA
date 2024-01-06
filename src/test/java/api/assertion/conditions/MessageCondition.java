package api.assertion.conditions;

import api.assertion.Condition;
import api.modelApi.swagger.Info;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.equalTo;

@RequiredArgsConstructor
public class MessageCondition implements Condition {
    private final String expectedMessage;
    @Override
    public void check(ValidatableResponse response) {
        Info info = response.extract().jsonPath().getObject("info", Info.class);
        Assertions.assertEquals(expectedMessage, info.getMessage());

//                response.body("info.message", equalTo(expectedMessage));
    }
}
