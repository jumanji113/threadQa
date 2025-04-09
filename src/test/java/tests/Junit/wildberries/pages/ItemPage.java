package tests.Junit.wildberries.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ItemPage {
    private WebDriver driver;
    private final By titleItem = By.xpath("//h1[@class='product-page__title']");
    private final By itemPriceText = By.xpath("(//span[@class='price-block__price'])[1]");


    public ItemPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getItemName(){
        return driver.findElement(titleItem).getText();
    }

    public Integer getItemPrice(){
        String priceText = driver.findElement(itemPriceText).getText();
        String formattedString = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(formattedString);
    }
}
