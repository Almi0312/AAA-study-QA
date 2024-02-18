package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardElementsTests {
    private WebDriver driver;

    @BeforeAll
    public static void downloadDriver(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        //если тест не грузится за это время, то тест падает
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        //если за это время не будет найден элемент на странице, то тест падает
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.get("http://85.192.34.140:8081/");
    }

    @AfterEach
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    @Test //Alert
    public void basicAuthTest(){
//        driver.get("https://the-internet.herokuapp.com/basic_auth");
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");//Так происходит аутентификая у алертов
        String h3 = driver.findElement(By.xpath("//h3")).getText();
        Assertions.assertEquals("Basic Auth", h3);
    }
    @Test
    public void alertOk(){
        String expectedText = "I am a JS Alert";
        driver.get("http://the-internet.herokuapp.com/javascript_alerts");
        driver.findElement(By.xpath("//button[@onclick='jsAlert()']")).click();
        String actualText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();

        Assertions.assertEquals(expectedText, actualText);
    }

    @Test
    public void iFrameTest(){
        driver.get("<url какого то сайта>");
        driver.findElement(By.xpath("<переход на фрейм, обычно это кнопка>")).click();

        WebElement iframe = driver.findElement(By.xpath("<поиск по метаданным фрейма и запись его xpath>"));
        driver.switchTo().frame(iframe); // переход на фрейм, для поиска нужных полей

        WebElement element = driver.findElement(By.xpath("<какой то путь внутри фрейма>"));;
    }
    /**
     * Ставится дебаггер в панели разработчика через 3 секунды
     * setTimeout(function() {
     *     debugger;
     *     }, 3000);
     */

    @Test
    public void sliderTest(){
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Slider']"));
        elementsTextBox.click();

        WebElement slider = driver.findElement(By.xpath("//input[@type='range']"));
//        Actions actions = new Actions(driver);
//        actions.dragAndDropBy(slider, 50,0)
//                .build().perform();

        int expectedValue = 85;
        int currentValue = Integer.parseInt(slider.getAttribute("value"));
        int valueToMove = expectedValue-currentValue;
        for (int i = 0; i < valueToMove; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }

        WebElement sliderValue = driver.findElement(By.id("sliderValue"));
        int actualValueInteger = Integer.parseInt(sliderValue.getAttribute("value"));;
        Assertions.assertEquals(expectedValue, actualValueInteger);
    }

    @Test
    public void hoverTest(){
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Menu']"));
        elementsTextBox.click();

        WebElement menuItemMiddle = driver.findElement(By.xpath("//a[text()='Main Item 2']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(menuItemMiddle).build().perform();
        WebElement subSubList = driver.findElement(By.xpath("//a[text()='SUB SUB LIST »']"));
        actions.moveToElement(subSubList).build().perform();

        List<WebElement> listElements = driver.findElements(By.xpath(" //a[contains(text(),'Sub Sub Item')]"));
        Assertions.assertEquals(2, listElements.size());
    }
}
