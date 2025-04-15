package tests.Junit.pageobjects.wildberries.uniticket;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.Junit.pageobjects.wildberries.BasePage;

public class UtMainPage extends BasePage {
    private By cityFromField = By.xpath("//input[@placeholder = 'Откуда']");
    private By listOfCityFrom = By.xpath("//div[@class='origin field active']//div[@class='city']");
    private By cityToField = By.xpath("//input[@placeholder='Куда']");
    private By listOfCityTo = By.xpath("//div[@class='destination field active']//div[@class='city']");
    private By dateTo = By.xpath("//input[@placeholder = 'Туда']");
    private By dateBack = By.xpath("//input[@placeholder = 'Обратно']");
    private String dateInCalendar = "//span[text() = '%d'";
    private By passangers = By.xpath("//span[@class = 'passengers']");
    private By searchButton = By.xpath("//div[@class = 'search_btn']");


    public UtMainPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.presenceOfElementLocated(cityFromField));
        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
    }

    public UtMainPage setCityFrom(String city){
        driver.findElement(cityFromField).clear();
        driver.findElement(cityFromField).sendKeys(city);
        driver.findElement(cityFromField).click();
        waitForTextPresentInList(listOfCityFrom, city).click();
        return this;
    }

    public UtMainPage setCityTo(String city){
        driver.findElement(cityToField).clear();
        driver.findElement(cityToField).sendKeys(city);
        driver.findElement(cityToField).click();
        waitForTextPresentInList(listOfCityTo, city).click();
        return this;
    }

    public UtMainPage setDayTo(int day){
        driver.findElement(dateTo).click();
        getDay(day).click();
        return this;
    }

    public UtMainPage setDayBack(int day){
        driver.findElement(dateBack).click();
        getDay(day).click();
        return this;
    }

    public UtMainPage searchTour(){
        driver.findElement(searchButton).click();
        return this;
    }

    private WebElement getDay(int day){
        By dayLocator = By.xpath(String.format(dateInCalendar, day));
        return driver.findElement(dayLocator);
    }
}
