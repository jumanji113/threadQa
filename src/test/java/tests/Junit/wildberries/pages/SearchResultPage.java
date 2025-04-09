package tests.Junit.wildberries.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResultPage {
    private WebDriver driver;
    private By allFilterButton = By.xpath("//div//button[contains(@class, 'dropdown-filter') and normalize-space(text())= 'Все фильтры']");
    private By priceOver = By.xpath("//input[@name= 'endN']");
    private By priceStart = By.xpath("//input[@name= 'startN']");
    private By showResultsButton = By.xpath("//button[@class='filters-desktop__btn-main btn-main']");
    private By items = By.xpath("//div[@class='product-card-list']//article");


    public SearchResultPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openItem(){
        driver.findElements(items).get(0).click();
                //.stream()
                //.filter(x->x.getText().contains("Iphone 13")).findAny()
                //.orElseThrow(() ->new NoSuchElementException("Не найден товер"))
                //.click();
    }

    public void openFilters(){
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(30));
        WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(allFilterButton));
        filterButton.click();
    }

    public void setMinPrice(Integer minValue){
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement priceStartElement = wait.until(ExpectedConditions.elementToBeClickable(priceStart));
        priceStartElement.clear();
        priceStartElement.sendKeys(String.valueOf(minValue));
    }

    public void setPriceOver(Integer maxValue){
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement priceOverElement = wait.until(ExpectedConditions.elementToBeClickable(priceOver));
        priceOverElement.clear();
        priceOverElement.sendKeys(String.valueOf(maxValue));
    }

    public void showResult(){
        driver.findElement(showResultsButton).click();
    }
}
