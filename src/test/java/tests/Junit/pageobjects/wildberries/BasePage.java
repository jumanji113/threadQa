package tests.Junit.pageobjects.wildberries;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;


    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    //имея кривой текст с большим количеством пробелов мы заменим все , что не является цифрой
    public String getTextJs(By elem){
        return (String)js.executeScript("return arguments[0].textContent;",
                driver.findElement(elem));
    }

    public void jsClick(By elem){
        js.executeScript("arguments[0].click;",
                driver.findElement(elem));
    }

    public void waitPageLoadWb(){
        By pageLoader = By.xpath("//div[@class='general-preloader']");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoader));
    }

    public void waitElementUpload(By locator){
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(locator)));
    }

    public void clearTextFieldFull(By locator) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(Keys.LEFT_CONTROL + "A");
        driver.findElement(locator).sendKeys(Keys.BACK_SPACE);
    }

    public WebElement waitForTextPresentInList(By locator, String value){
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator)
                .stream()
                .filter(x->x.getText().contains(value))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Города нет " + value));
    }

    public void waitForTextMatchesRegex(By locator, String regex) {
        Pattern pattern = Pattern.compile(regex);
        wait.until(ExpectedConditions.textMatches(locator, pattern));
    }

    public void waitForElementDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForElementAppear(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public Integer getDigitFromWebElement(WebElement element) {
        String text = element.getText().replaceAll("[^0-9.]", "");
        return Integer.parseInt(text);
    }

    public List<Integer> getDigitsFromList(By locator) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        return driver.findElements(locator).stream()
                .filter(x -> x.isDisplayed())
                .map(x -> getDigitFromWebElement(x))
                .collect(Collectors.toList());
    }
}
