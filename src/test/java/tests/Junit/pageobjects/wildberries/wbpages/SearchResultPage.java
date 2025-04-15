package tests.Junit.pageobjects.wildberries.wbpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.Junit.pageobjects.wildberries.BasePage;

public class SearchResultPage extends BasePage {
    private By allFilterButton = By.xpath("//div//button[contains(@class, 'dropdown-filter') and normalize-space(text())= 'Все фильтры']");
    private By priceOver = By.xpath("//input[@name= 'endN']");
    private By priceStart = By.xpath("//input[@name= 'startN']");
    private By showResultsButton = By.xpath("//button[@class='filters-desktop__btn-main btn-main']");
    private By items = By.xpath("//div[@class='product-card-list']//article");



    public ItemPage openItem() {
        driver.findElements(items).get(0).click();
        waitPageLoadWb();
        return new ItemPage(driver);
    }


    public SearchResultPage(WebDriver driver) {
        super(driver);
    }

    public SearchResultPage openFilters(){
        waitElementUpload(allFilterButton);
        driver.findElement(allFilterButton).click();
        waitElementUpload(items);
        return this;
    }

    public SearchResultPage setMinPrice(Integer minValue){
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement priceStartElement = wait.until(ExpectedConditions.elementToBeClickable(priceStart));
        priceStartElement.clear();
        priceStartElement.sendKeys(String.valueOf(minValue));
        return this;
    }

    public SearchResultPage setPriceOver(Integer maxValue){
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement priceOverElement = wait.until(ExpectedConditions.elementToBeClickable(priceOver));
        priceOverElement.clear();
        priceOverElement.sendKeys(String.valueOf(maxValue));
        return this;
    }

    public SearchResultPage showResult(){
        driver.findElement(showResultsButton).click();
        return this;
    }
}
