package ui.pageObject.wildberries.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ItemPage  extends BasePage{
    private By itemHeaderName = By.xpath("//h1[text()='iPhone 12 128GB']");
    private By itemPriceText = By.xpath("//span[@class='price-block__price']");

    public ItemPage(WebDriver driver) {
        super(driver);
    }


    public String getItemName(){
        return driver.findElement(itemHeaderName).getText();
    }

    public Integer getItemPrice(){
        String priceText = getTextJs(itemPriceText);
        return Integer.parseInt(priceText.replaceAll("[^/d.]",""));
    }

}
