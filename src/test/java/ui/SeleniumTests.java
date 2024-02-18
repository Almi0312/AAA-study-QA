package ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SeleniumTests {
    private WebDriver driver;
    private String downloadFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "downloadFiles";

    @BeforeAll
    public static void downloadDriver(){
        WebDriverManager.chromedriver().setup();
    }
    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        Map<String,String> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder);
        options.setExperimentalOption("prefs", prefs);


        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        //если тест не грузится за это время, то тест падает
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        //если за это время не будет найден элемент на странице, то тест падает
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    }

    @AfterEach
    public void tearDown() {
        driver.close();
        driver.quit();
    }


    @ParameterizedTest
    @CsvSource({
            "Tomas Anderson, tomas@matrix.ru," +
                    " USA Los Angeles, USA Miami"
    })
    public void simpleFormTest(String expectedName, String expectedEmail,
                               String expectedCurrentAddress, String expectedPermanentAddress) {
        driver.get("http://85.192.34.140:8081/");

        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Text Box']"));
        elementsTextBox.click();

        WebElement fullName = driver.findElement(By.xpath("//input[@id='userName']"));
        WebElement fullEmail = driver.findElement(By.xpath("//input[@id='userEmail']"));
        WebElement fullCurrentAddress = driver.findElement(By.xpath("//textarea[@id='currentAddress']"));
        WebElement fullPermanentAddress = driver.findElement(By.xpath("//textarea[@id='permanentAddress']"));
        WebElement submit = driver.findElement(By.xpath("//button[@id='submit']"));

        fullName.sendKeys("Tomas Anderson");
        fullEmail.sendKeys("tomas@matrix.ru");
        fullCurrentAddress.sendKeys("USA Los Angeles");
        fullPermanentAddress.sendKeys("USA Miami");
        submit.click();

        WebElement name = driver.findElement(By.xpath("//p[@id='name']"));
        WebElement email = driver.findElement(By.xpath("//p[@id='email']"));
        WebElement currentAddress = driver.findElement(By.xpath("//p[@id='currentAddress']"));
        WebElement permanentAddress = driver.findElement(By.xpath("//p[@id='permanentAddress']"));

        Assertions.assertTrue(name.getText().contains(expectedName));
        Assertions.assertTrue(email.getText().contains(expectedEmail));
        Assertions.assertTrue(currentAddress.getText().contains(expectedCurrentAddress));
        Assertions.assertTrue(permanentAddress.getText().contains(expectedPermanentAddress));
    }

    @Test
    public void uploadFileTest(){
        driver.get("http://85.192.34.140:8081/");

        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementsTextBox.click();

        WebElement uploadFile = driver.findElement(By.xpath("//input[@id='uploadFile']"));
        uploadFile.sendKeys(System.getProperty("user.dir") + "/src/test/resources/star.jpg");
        WebElement uploadFakePath = driver.findElement(By.xpath("//p[@id='uploadedFilePath']"));
        Assertions.assertTrue(uploadFakePath.getText().contains("star.jpg"));
    }

    @Test
    public void downloadFileTest(){
        driver.get("http://85.192.34.140:8081/");

        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text()='Upload and Download']"));
        elementsTextBox.click();
        WebElement downloadBtn = driver.findElement(By.id("downloadButton"));
        downloadBtn.click();

        //Ожидание для скачивания файла
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(x-> Paths.get(downloadFolder, "sticker.png").toFile().exists());

        File file = new File("build/downloadFiles/sticker.png");
        Assertions.assertTrue(file.length() != 0);
        Assertions.assertNotNull(file);
    }
}