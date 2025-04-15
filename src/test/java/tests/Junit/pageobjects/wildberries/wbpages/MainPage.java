package tests.Junit.pageobjects.wildberries.wbpages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import tests.Junit.pageobjects.wildberries.BasePage;

public class MainPage extends BasePage {
    private final By searchField = (By.id("searchInput"));
    private final By crtBtnBasket = By.xpath("//a[@data-wba-header-name = 'Cart']");
    private final By loginBtn = By.xpath("//a[@data-wba-header-name = 'Login']");

    public MainPage(WebDriver driver) {
        super(driver);
        waitPageLoadWb();
    }

    public SearchResultPage searchItem(String item){
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(item);
        driver.findElement(searchField).sendKeys(Keys.ENTER);

        return new SearchResultPage(driver);
    }


}
