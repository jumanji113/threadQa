package tests.Junit.pageobjects.wildberries.wbpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.Junit.pageobjects.wildberries.BasePage;

public class ItemPage extends BasePage {
    private final By titleItem = By.xpath("//h1[@class='product-page__title']");
    private final By itemPriceText = By.xpath("(//span[@class='price-block__price'])[1]");


    public ItemPage(WebDriver driver) {
        super(driver);
    }

    public String getItemName(){
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(titleItem));
        return driver.findElement(titleItem).getText();
    }

    public Integer getItemPrice(){
        String priceText = getTextJs(itemPriceText);
        String formattedString = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(formattedString);
    }
}
