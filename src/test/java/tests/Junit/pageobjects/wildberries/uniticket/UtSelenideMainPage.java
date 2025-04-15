package tests.Junit.pageobjects.wildberries.uniticket;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.*;

public class UtSelenideMainPage {
    private SelenideElement cityFromField = $x("//input[@placeholder = 'Откуда']");
    private ElementsCollection listOfCityFrom =  $$x("//div[@class='origin field active']//div[@class='city']");
    private SelenideElement cityToField =  $x("//input[@placeholder='Куда']");
    private ElementsCollection listOfCityTo =  $$x("//div[@class='destination field active']//div[@class='city']");
    private SelenideElement dateTo =  $x("//input[@placeholder = 'Туда']");
    private SelenideElement dateBack =  $x("//input[@placeholder = 'Обратно']");
    private String dateInCalendar = "//span[text() = '%d'";
    private SelenideElement passangers =  $x("//span[@class = 'passengers']");
    private SelenideElement searchButton =  $x("//div[@class = 'search_btn']");

    public UtSelenideMainPage setCityFrom(String city){
        cityFromField.clear();
        cityFromField.sendKeys(city);
        cityFromField.click();
        listOfCityFrom.find(Condition.partialText(city)).click();
        return this;
    }

    public UtSelenideMainPage setCityTo(String city){
        cityToField.clear();
        cityToField.sendKeys(city);
        cityToField.click();
        listOfCityTo.find(Condition.partialText(city)).click();
        return this;
    }

    public UtSelenideMainPage setDayTo(int day){
        dateTo.click();
        getDay(day).click();
        return this;
    }

    public UtSelenideMainPage setDayBack(int day){
        dateBack.click();
        getDay(day).click();
        return this;
    }

    public UtSearchSelenidePage searchTour(){
        searchButton.click();
        //return new UtSearchSelenidePage();
        return page(UtSearchSelenidePage.class);
    }

    private SelenideElement getDay(int day){
        return $x(String.format(dateInCalendar, day));
    }
}
