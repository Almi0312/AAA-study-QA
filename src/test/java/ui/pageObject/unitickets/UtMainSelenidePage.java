package ui.pageObject.unitickets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebDriver;
import ui.pageObject.wildberries.pages.BasePage;

import static com.codeborne.selenide.Selenide.*;

public class UtMainSelenidePage {
    private SelenideElement cityFromField = $x("//input[@placeholder='Откуда']");
    private ElementsCollection listOfCityFrom = $$x("//div[@class='origin field active']//div[@class='city']");
    private SelenideElement cityToField = $x("//input[@placeholder='Куда']");
    private ElementsCollection listOfCityTo = $$x("//div[@class='destination field active']//div[@class='city']");
    private SelenideElement dateForward = $x("//input[@placeholder='Туда']");
    private SelenideElement dateBack = $x("//input[@placeholder='Обратно']");
    private String dayInCalendar = "//span[text()='%d']";
    private SelenideElement searchBtn = $x("//div[@class='search_btn']");


    public UtMainSelenidePage setCityFrom(String city){
        cityFromField.clear();
        cityFromField.sendKeys(city);
        cityFromField.click();
        listOfCityFrom.find(Condition.partialText(city)).click();
        return this;
    }

    public UtMainSelenidePage setCityTo(String city){
        cityToField.clear();
        cityToField.sendKeys(city);
        cityToField.click();
        listOfCityTo.find(Condition.partialText(city)).click();;
        return this;
    }

    public UtMainSelenidePage setDayForward(int day){
        dateForward.click();
        getDay(day).click();
        $x("//h2").click();
        return this;
    }

    public UtMainSelenidePage setDayBack(int day){
        dateBack.click();
        getDay(day).click();
        $x("//h2").click();
        return this;
    }

    public UtSearchSelenidePage search(){
        searchBtn.click();
        return page(UtSearchSelenidePage.class);
    }

    private SelenideElement getDay(int day){
        return $x(String.format(dayInCalendar, day));
    }
}
