package listener;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, который служит для повтора нестабильных тестов и
 * записывает упавшие тесты в файл. Реализация для JUnit
 * TestExecutionExceptionHandler отвечает за падение теста
 * Отвечает за обработку всех ошибок
 * AfterTestExecutionCallback при падении теста записывает его в файл
 * Выполняет логику после завершения теста
 */
public class RetryListenerJUnit implements TestExecutionExceptionHandler,
                                      AfterTestExecutionCallback {
    //количество прогона теста в случае неудачи
    private static final int MAX_RETRIES = 3;
    //Записывает имена тестов
    private static final Set<String> failedTestNamed = new HashSet<>();

    @Override
    public void handleTestExecutionException(ExtensionContext extensionContext, Throwable throwable) throws Throwable {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                extensionContext.getRequiredTestMethod().invoke(extensionContext.getRequiredTestInstance());
                return;
            } catch (Throwable ex) {
                //getCause - указание причины почему упали
                throwable = ex.getCause();
            }
        }
        throw throwable;
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        Method method = extensionContext.getRequiredTestMethod();
        String testClass = extensionContext.getRequiredTestClass().getName();
        String testName = method.getName();
        String testToWrite =  String.format("--tests %s.%s*\n",testClass, testName);
        extensionContext.getExecutionException().ifPresent(x ->
                failedTestNamed.add(testToWrite));
    }

    public static void saveFailedTests() throws IOException {
        String output = "src/test/resources/FailedTests.txt";
        String result = String.join(" ", failedTestNamed);

        FileUtils.writeStringToFile(new File(output), result, StandardCharsets.UTF_8);
//        try(FileWriter writer = new FileWriter(output,true)){
//            writer.write(result);
//        }
    }
}
