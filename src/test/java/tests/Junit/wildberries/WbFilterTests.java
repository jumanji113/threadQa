package tests.Junit.wildberries;

import org.junit.jupiter.api.Test;
import tests.Junit.wildberries.pages.ItemPage;
import tests.Junit.wildberries.pages.MainPage;
import tests.Junit.wildberries.pages.SearchResultPage;

public class WbFilterTests extends BaseTest{

    @Test
    public void searchResultTest() throws InterruptedException {
        String expectedItem = "Iphone";
        Integer expectedStartPrice = 10000;
        Integer expectedOverPrice = 70000;
        MainPage page = new MainPage(driver);
        page.searchItem(expectedItem);
        Thread.sleep(1000);
        SearchResultPage searchResultPage = new SearchResultPage(driver);
        searchResultPage.openFilters();
        searchResultPage.setMinPrice(expectedStartPrice);
        searchResultPage.setPriceOver(expectedOverPrice);
        searchResultPage.showResult();
        searchResultPage.openItem();

        ItemPage itemPage = new ItemPage(driver);
    }



}
