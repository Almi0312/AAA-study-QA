package ui.pageObject.wildberries.pages;

import org.openqa.selenium.*;

public class SearchResultPage extends BasePage{

    private By allFiltersButton = By.xpath("//button[@class='dropdown-filter__btn dropdown-filter__btn--all']");
    private By endPriceField = By.xpath("//input[@name='endN']");
    private By startPriceField = By.xpath("//input[@name='startN']");
    private By applyButton = By.xpath(" //button[text()='Показать']");
    private By items = By.xpath(" //div[@class='product-card-list']//article");

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }


    public SearchResultPage openFilters(){
        driver.findElement(allFiltersButton).click();
        waitPageLoads();
        return this;
    }

    public SearchResultPage setEndPriceField(Integer maxPrice){
        clearText(endPriceField);
        driver.findElement(endPriceField).sendKeys(String.valueOf(maxPrice));
        return this;
    }

    public SearchResultPage setStartPriceField(Integer minPrice){
        clearText(startPriceField);
        driver.findElement(startPriceField).sendKeys(String.valueOf(minPrice));
        return this;
    }

    public SearchResultPage applyFilters(){
        driver.findElement(applyButton).click();
        return this;
    }

    public ItemPage openItems(){
        driver.findElements(items).get(0).click();
        waitPageLoads();
        return new ItemPage(driver);
//        driver.findElements(items).stream()
//                .filter(x->x.getText().contains("iPhone 11"))
//                .findAny().
//                orElseThrow(()-> new NoSuchContextException("Не найден товар"))
//                .click();
    }
}
