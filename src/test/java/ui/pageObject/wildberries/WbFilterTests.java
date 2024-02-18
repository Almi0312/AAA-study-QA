package ui.pageObject.wildberries;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ui.pageObject.BaseTest;
import ui.pageObject.wildberries.pages.ItemPage;
import ui.pageObject.wildberries.pages.MainPage;
import ui.pageObject.wildberries.pages.SearchResultPage;

public class WbFilterTests extends BaseTest {

    @Test
    public void searchResultTest(){
        ItemPage itemPage = new MainPage(driver)
                .searchItem("iPhone")
                .openFilters()
                .setEndPriceField(100000)
                .setStartPriceField(30000)
                .applyFilters()
                .openItems();


        String actualName = itemPage.getItemName();
        Integer actualPrice = itemPage.getItemPrice();
        Assertions.assertTrue(actualName.contains("iPhone"));
        Assertions.assertTrue(30000 <= actualPrice && actualPrice <= 100000);

    }
}
