package listener;

import org.apache.commons.io.FileUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, который служит для повтора нестабильных тестов и
 * записывает упавшие тесты в файл. Реализация для testNG
 * IRetryAnalyzer служит для аналитики попыток и добавления количества попыток
 * ITestListener служит для наблюдения за тестом
 */
public class RetryListenerTestNG implements IRetryAnalyzer, ITestListener {
    private final int MAX_RETRIES = 2;
    private int count = 0;
    private static final Set<String> failedTestNamed = new HashSet<>();

    @Override
    public boolean retry(ITestResult iTestResult) {
        if(count < MAX_RETRIES){
            count++;
            return true;
        }
        return false;
    }
    private void addToFailedSet(ITestResult result){
        String testClass = result.getTestClass().getName();     //Получаем имя класса с тестом
        String testName = result.getName();                     //Получаем имя теста
        String testToWrite =  String.format
                ("--tests %s.%s",testClass, testName);        //формируем название теста
        failedTestNamed.add(testToWrite);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        addToFailedSet(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        addToFailedSet(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        addToFailedSet(result);
    }

    public static void saveFailedTests() throws IOException {
        String output = "src/test/resources/FailedTestsNG.txt";
        String result = String.join(" ", failedTestNamed);

        FileUtils.writeStringToFile(new File(output), result, StandardCharsets.UTF_8);
    }


        @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }
}
