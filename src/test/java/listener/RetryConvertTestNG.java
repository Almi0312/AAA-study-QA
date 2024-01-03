package listener;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Класс, служащий для конвертирования слушателя RetryConvertTestNG в другой класс
 * для запуска повторных прогонов тестов через testng.xml файл
 */
public class RetryConvertTestNG implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryListenerTestNG.class);
    }
}
