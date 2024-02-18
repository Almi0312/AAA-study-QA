package ui.pageObject.unitickets;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ui.pageObject.wildberries.pages.BasePage;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class UtSearchSelenidePage {

    private SelenideElement titleLoader = $x("//div[@class='countdown-title']");
    private SelenideElement priceSelectedMain = $x("//li[@class='price--current']//span[@class='prices__price currency_font currency_font--rub']");
    private SelenideElement selectedDayForward = $x("//li[@class-'price--current']//a/span[1]");
    private SelenideElement selectedDayBack = $x("//li[@class='price--current']//a/span[3]");
    private ElementsCollection listOfForwardDays = $$x("//div[@class='ticket-action-airline-container']//following::span[@class='flight-brief-date__day'][1]]");
    private ElementsCollection listOfBackDays = $$x("//div[@class='ticket-action-airline-container']//following::span[@class='flight-brief-date__day'][3]");

    public UtSearchSelenidePage assertAllDaysForwardHaveDay(int expectedDay){
        String day = String.valueOf(expectedDay);
        //Метод проверки наличия дня среди элементов
        listOfForwardDays.should(CollectionCondition.containExactTextsCaseSensitive(day));
        return this;
    }

    public UtSearchSelenidePage assertAllDaysBackHaveDay(int expectedBackDay){
        String day = String.valueOf(expectedBackDay);
        //Метод проверки наличия дня среди элементов
        listOfBackDays.should(CollectionCondition.containExactTextsCaseSensitive(day));
        return this;
    }

    //сравниваем дату
    public UtSearchSelenidePage assertMainDayForward(int expectedDay){
        selectedDayForward.should(Condition.partialText(String.valueOf(expectedDay)));
        return this;
    }

    //ищем по заданному тексту
    public UtSearchSelenidePage assertMainDayBack(int expectedDay){
        selectedDayBack.should(Condition.partialText(String.valueOf(expectedDay)));
        return this;
    }

    //ждем загрузки сайта
    public UtSearchSelenidePage waitForPage(){
        priceSelectedMain.should(Condition.matchText("\\d+"));
        return this;
    }

    public UtSearchSelenidePage waitForTitleDisapear(){
        titleLoader.should(Condition.disappear, Duration.ofSeconds(30));
        return this;
    }
}
