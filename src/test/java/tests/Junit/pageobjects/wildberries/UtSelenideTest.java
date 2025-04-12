package tests.Junit.pageobjects.wildberries;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

public class UtSelenideTest {
    @Test
    public void firstSelenideTest(){
        Selenide.open("https://uniticket.ru/");
        Selenide.closeWebDriver();
    }
}
