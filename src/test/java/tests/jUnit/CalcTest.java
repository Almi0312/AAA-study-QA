package tests.jUnit;

import cals.CalcSteps;
import io.qameta.allure.Allure;
import io.qameta.allure.Issue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.atomic.AtomicInteger;


public class CalcTest {
    @DisplayName("шаги вне метода с суммой 2 значений")
    @Issue("VIDEOTECH-5613")
    @ParameterizedTest
    @CsvSource({
            "2, 2",
            "4, 4",
            "-2, -3"
    })
    public void sumTest(String number1, String number2){
        CalcSteps calcSteps = new CalcSteps();
        int a = Integer.parseInt(number1);
        int b = Integer.parseInt(number2);
        int result = calcSteps.sum(a,b);
        boolean isOk = calcSteps.isPositive(result);
        Assertions.assertTrue(isOk);
    }

    @Test
    @Issue("VIDEOTECH-5612")
    @DisplayName("шаги внутри метода с суммой 2 значений")
    public void sumStepsTest(){
        int a = 5;
        int b = 4;
        AtomicInteger result = new AtomicInteger();
        Allure.step(String.format("Прибавляем %d к переменной %d", a, b),
                step -> {
            result.set(a+b);
                });
        Allure.step("Проверяем, что результат " + result.get() + " больше нуля",
                step -> {
            Assertions.assertTrue(result.get() > 0);
                });
    }
}
