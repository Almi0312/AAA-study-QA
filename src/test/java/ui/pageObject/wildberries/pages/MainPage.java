package ui.pageObject.wildberries.pages;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MainPage extends BasePage{
    private By searchField = By.id("searchInput");
    private By bucketBtn = By.xpath("//a[@data-wba-header-name='Cart']");
    private By loginBtn = By.xpath("//a[@data-wba-header-name='Login']");

    public MainPage(WebDriver driver) {
        super(driver);
        waitPageLoads();
    }


    public SearchResultPage searchItem(String item){
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(item);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        return new SearchResultPage(driver);

    }

    public void waitForElementUpdated(By locator){
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(locator)));
    }
}
