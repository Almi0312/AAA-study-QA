package tests.testNG;

import cals.CalcSteps;
import io.qameta.allure.Allure;
import listener.RetryListenerTestNG;
import models.People;
import org.junit.jupiter.api.DisplayName;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Listeners(RetryListenerTestNG.class) //применяется логика сохранения упавших тестов
public class NGTest {
    /**
     * Данный метод служит для того, что бы не писать в аннотации @Test
     * retryAnalizer=<Какой то класс слушатель с повтором>, а запускать этот класс
     * для всех методов в данном классе
     * @param context переменная, которая содержит в себе информацию о тестовых
     *                методах
     */
    @BeforeSuite
    public void setAnalyzer(ITestContext context){
        for(ITestNGMethod testNGMethod : context.getAllTestMethods()){
            testNGMethod.setRetryAnalyzerClass(RetryListenerTestNG.class);
        }

    }
    @AfterSuite
    public void tearDown() throws IOException {
        RetryListenerTestNG.saveFailedTests(); //сохраняет упавшие тесты в файл
    }

//    @Test(retryAnalyzer = RetryListenerTestNG.class)
    @DisplayName("сумма 2 чисел")
    @Test(groups = "sum1")
    public void firstTestNG1(){
        CalcSteps calcSteps = new CalcSteps();
        int result = calcSteps.sum(1,1);
        Assert.assertEquals(4, result);
    }

//    @Test(retryAnalyzer = RetryListenerTestNG.class)
    @Test(groups = "sum")
    @DisplayName("сумма 2 чисел")
    public void firstTestNG2(){
        CalcSteps calcSteps = new CalcSteps();
        int result = calcSteps.sum(2,2);
        Assert.assertEquals(3, result);
    }

    @DataProvider(name = "testUsers")
    public Object[] initData(){
        People stas = new People("Stas", 25, "male");
        People katya = new People("Katya", 19, "female");
        People oleg = new People("Oleg", 26, "male");
        return new Object[]{
                stas,
                katya,
                oleg
        };
    }

    @Test(dataProvider = "testUsers")
    @DisplayName("Проверка на возраст")
    public void testUsersWithRole(People people){
        Assert.assertTrue(people.getAge()> 18);
        //some magic
        Assert.assertTrue(people.getName().contains("a"));
    }

    @DataProvider(name = "ips")
    public Object[] testIpAddresses(){
        List<String> ips = new ArrayList<>();
        ips.add("127.0.0.1");
        ips.add("localhost");
        ips.add("58.43.121.90");

//        return ips.stream().map(x ->
//                new Object[]{x})
//                .toArray(Object[][]::new);
        return ips.toArray();
    }

    @Test(dataProvider = "ips")
    public void ipsTest(String ip){
        Allure.step(ip + " является ip", step -> {
                Assert.assertTrue(ip.matches("^([0-9]+(\\.|$)){4}"));});
    }

    @Test(dataProviderClass = DataTestArguments.class, dataProvider = "argsForCalc")
    public void calcTest1(int a, int b, int c){
        Assert.assertEquals(new CalcSteps().sum(a,b), c);
    }

    @Test(dataProviderClass = DataTestArguments.class, dataProvider = "diffArgs")
    public void calcTest2WithObjectArguments(int a, String b){

        Assert.assertEquals(convert(a), b);
    }

    private String convert(int a){
        switch (a){
            case 1: return "one";
            case 2: return "two";
            case 3: return "three";
            case 5: return "five";
            default: return null;
        }
    }
}
