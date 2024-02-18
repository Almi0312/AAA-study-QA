package ui.pageObject;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import ui.pageObject.unitickets.UtMainSelenidePage;

public class UtSelenideTest {
    @Test
    public void firstSelenideTest(){
        Selenide.open("https://uniticket.ru/");
        UtMainSelenidePage mainPage = new UtMainSelenidePage();
        mainPage.setCityFrom("Казань")
                .setCityTo("Дубай")
                .setDayBack(30)
                .setDayForward(25)
                .search()
                .waitForPage()
                .waitForTitleDisapear()
                .assertMainDayBack(30)
                .assertMainDayForward(25)
                .assertAllDaysBackHaveDay(30)
                .assertAllDaysForwardHaveDay(25);
    }
}
