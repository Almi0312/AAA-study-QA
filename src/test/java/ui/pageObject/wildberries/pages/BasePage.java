package ui.pageObject.wildberries.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    private By pageLoader = By.xpath("//div[@class='general-preloader']");

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;
    }

    public String getTextJs(By element){
        return (String)js.executeScript("return arguments[0].textContent;", driver.findElement(element));
    }

    public void jsClick(By element){
        js.executeScript("arguments[0].click",driver.findElement(element));
    }

    public void waitPageLoads(){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));
    }

    public void clearText(By locator){
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(Keys.LEFT_CONTROL + "A");
        driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
    }
}
