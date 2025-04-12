package tests.Junit.pageobjects.wildberries;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.Junit.pageobjects.wildberries.uniticket.UtMainPage;

public class UtFilterTest extends BaseTest{

    @BeforeEach
    public void openSite(){
        driver.get("https://uniticket.ru/");
    }

    @Test
    @SneakyThrows
    public void searchResultTest(){

        String expectedCityFrom = "Казань";
        String expectedCityTo = "Казань";
        Integer expectedDayTo = 24;
        Integer expectedDayBack = 25;

        UtMainPage utMainPage = new UtMainPage(driver);
        utMainPage
                .setCityFrom(expectedCityFrom)
                .setCityTo(expectedCityTo)
                .setDayTo(expectedDayTo)
                .setDayBack(expectedDayBack)
                .searchTour();

    }
}
