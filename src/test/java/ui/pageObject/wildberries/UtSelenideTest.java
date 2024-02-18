package ui.pageObject.wildberries;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class UtSelenideTest {
//    @BeforeEach
//    public void initSettings(){
//        Configuration.
//    }
    @Test
    public void firstSelenideTest(){
        Selenide.open("https://uniticket.ru/");
        SelenideElement h1 = $x("//h1");
        h1.should(Condition.text("Поиск дешевых авиабилетов"));

        ElementsCollection collection = $$x("//input");
        collection.find(Condition.partialText("Казань")).click();
        //collection.asDynamicIterable().stream() перебор коллекции
        collection.should(CollectionCondition.containExactTextsCaseSensitive());//проверки текста

    }

}
