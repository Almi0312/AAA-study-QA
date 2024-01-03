package tests.jUnit;

import listener.RetryListenerJUnit;
import models.People;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;
@Tag("API") //для запуска отдельных тестов
@ExtendWith(RetryListenerJUnit.class) //для запуска с каким либо классом
public class VerySimpleTest {
    //запуск отдельных тестов ./gradlew clean test --tests tests.jUnit.VerySimpleTest.testWithNameContainsS
    static int eachInitCount = 0;
    static int eachAfterCount = 0;
    static int allInitCount = 0;
    static int allAfterCount = 0;

    @BeforeEach
    public void init(){
        eachInitCount++;
        System.out.printf("each init test %d\n", eachInitCount);
    }
    @BeforeAll
    public static void allInit(){
        allInitCount++;
        System.out.printf("all init test %d\n", allInitCount);
    }

    @AfterEach
    public void tearDown(){
        eachAfterCount++;
        System.out.printf("each after test %d\n", eachAfterCount);
    }

    @AfterAll
    public static void allTearDown() throws IOException {
        allAfterCount++;
        System.out.printf("all after test %d\n", allAfterCount);
        RetryListenerJUnit.saveFailedTests();
    }


   @Test
   public void fasi(){
        int b = 6;
        Assertions.assertEquals(2,b);
   }

    @ParameterizedTest
    @CsvSource({
            "stas, 18, male",
            "sasha, 20, female",
            "misha, 10, male"
            })
    public void testWithNameContainsS(String name, String age, String sex){
        System.out.printf("%s %s %s\n", name, age, sex);
        Assertions.assertTrue(name.contains("s"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/people.csv", delimiter = ',')
    public void  testWithNameContainsA(String name, String age, String sex){
        System.out.printf("%s %s %s\n", name, age, sex);
        Assertions.assertTrue(name.contains("a"));
    }
//=================================================================
    private static Stream<Arguments> testPeople(){
        return Stream.of(
                Arguments.of(new People("stas", 18, "male")),
                Arguments.of(new People("sasha", 20, "female")),
                Arguments.of(new People("misha", 10, "male"))
        );
    }
    @ParameterizedTest
    @MethodSource("testPeople")
    public void  testWithNameContainsA(People people){
        System.out.printf("%s %s %s\n", people.getName(), people.getAge(), people.getSex());
        Assertions.assertTrue(people.getName().contains("a"));
    }

//=================================================================

    @Test
    @DisplayName("Просмотр разницы чисел")
    public void testTwoLessThanThree(){
        int a = 2;
        int b = 3;
        Assertions.assertTrue(a<b,
                String.format("Число %d больше чем число %d",a,b));
    }

    @Test
    @DisplayName("Сумма чисел")
//    @Disabled("Тест не критичен,исправим потом")
    public void sum(){
        int expect = 5;
        int actual = 3+2;
        Assertions.assertEquals(expect, actual);
    }
}
