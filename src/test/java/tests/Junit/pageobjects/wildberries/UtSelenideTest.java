package tests.Junit.pageobjects.wildberries;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.Junit.pageobjects.wildberries.uniticket.UtSelenideMainPage;

import static com.codeborne.selenide.Selenide.$x;

public class UtSelenideTest {

    @Test
    public void firstSelenideTest(){
        int expectedDayForward = 25;
        int expectedDayBack = 30;
        Selenide.open("https://uniticket.ru/");
        UtSelenideMainPage utSelenideMainPage = new UtSelenideMainPage();
        utSelenideMainPage
                .setCityFrom("казань")
                .setCityTo("дубай")
                .setDayTo(expectedDayForward)
                .setDayBack(expectedDayBack)
                .searchTour()
                .waitForPage()
                .waitForTitleDisappear()
                .assertDayBack(expectedDayBack)
                .assertDayForward(expectedDayForward)
                .assertAllDaysBack(expectedDayBack)
                .assertAllDaysForwardShouldHaveDay(expectedDayForward);
    }
}
