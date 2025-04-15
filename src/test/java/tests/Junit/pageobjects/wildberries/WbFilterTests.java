package tests.Junit.pageobjects.wildberries;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.Junit.pageobjects.wildberries.wbpages.ItemPage;
import tests.Junit.pageobjects.wildberries.wbpages.MainPage;

public class WbFilterTests extends BaseTest{

    @BeforeEach
    public void openSite(){
        driver.get("https://www.wildberries.ru/");
    }

    @Test
    @SneakyThrows
    public void searchResultTest()  {
        String expectedItem = "Iphone";
        Integer expectedStartPrice = 10000;
        Integer expectedOverPrice = 70000;
        ItemPage itemPage = new MainPage(driver)
                .searchItem(expectedItem)
                .openFilters()
                .setMinPrice(expectedStartPrice)
                .setPriceOver(expectedOverPrice)
                .showResult()
                .openItem();

        String actualText = itemPage.getItemName();
        Integer actualPrice = itemPage.getItemPrice();

        Assertions.assertTrue(actualText.toLowerCase().contains(expectedItem.toLowerCase()));
        Assertions.assertTrue(actualPrice >=expectedStartPrice && actualPrice <= expectedOverPrice);
    }
}
