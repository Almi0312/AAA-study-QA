package ui.pageObject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;

    @BeforeAll
    public static void downloadDriver(){
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        //если тест не грузится за это время, то тест падает
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        //если за это время не будет найден элемент на странице, то тест падает
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.get("https://www.wildberries.ru/");
    }

    @AfterEach
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
