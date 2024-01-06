package api.assertion;

import api.assertion.conditions.MessageCondition;
import api.assertion.conditions.StatusCodeCondition;

/**
 * Класс с методами проверки условий
 */
public class Conditions {
    public static MessageCondition hasMessage(String expectedMassage){
        return new MessageCondition(expectedMassage);
    }

    public static StatusCodeCondition hasStatusCode(Integer expectedStatus){
        return new StatusCodeCondition(expectedStatus);
    }
}
