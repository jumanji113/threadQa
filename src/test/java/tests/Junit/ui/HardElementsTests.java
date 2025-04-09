package tests.Junit.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HardElementsTests {
    private WebDriver driver;

    @BeforeAll
    public static void downloadChromeDriver(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp(){
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(40));
    }

    @AfterEach
    public void tearDown(){
        driver.close();
    }

    @Test
    public void authTest(){
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        String h3 = driver.findElement(By.xpath("//div[@class='example']/h3")).getText();
        Assertions.assertEquals("Basic Auth", h3);
    }

    @Test
    public void alertOk(){
        String expectedText = "I am a JS Alert";
        String expectedResult = "You successfully clicked an alert";
        driver.get("https://the-internet.herokuapp.com/javascript_alerts");
        driver.findElement(By.xpath("//button[@onClick='jsAlert()']")).click();
        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        Assertions.assertEquals(expectedText, alertText);
        String actualResult = driver.findElement(By.xpath("//p[@id='result']")).getText();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void iframeTest(){
        driver.get("https://mail.ru/");
        driver.findElement(By.xpath("//button[@data-testid='enter-mail-primary']")).click();

        WebElement iframeAuth = driver.findElement(By.xpath("ag-popup__frame__layout__iframe"));
        driver.switchTo().frame(iframeAuth);
    }

    @Test
    public void sliderTest(){
        driver.get("http://85.192.34.140:8081");
        WebElement widgets = driver.findElement(By.xpath("//div[@class='card-body']//h5[text() = 'Widgets']"));
        widgets.click();
        WebElement sliderMenu = driver.findElement(By.xpath("//span[text()='Slider']"));
        sliderMenu.click();
        WebElement slider = driver.findElement(By.xpath("//input[@type='range']"));
        //Actions actions = new Actions(driver);
        //actions.dragAndDropBy(slider, 50, 0).build().perform();// 1 вариант через actions/дальше через цикл
        int expectedSize = 80;
        int currentValue = Integer.parseInt(Objects.requireNonNull(slider.getAttribute("value")));
        int valueMoment = expectedSize - currentValue;
        for (int i = 0; i < valueMoment; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }

        WebElement inputValue = driver.findElement(By.id("sliderValue"));
        Assertions.assertEquals(expectedSize,Integer.parseInt(Objects.requireNonNull(inputValue.getAttribute("value"))));
    }

    @Test
    public void hoverTest() {
        driver.get("http://85.192.34.140:8081");

        // Переход к разделу Widgets
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement widgets = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-body']//h5[text() = 'Widgets']")));
        widgets.click();

        // Переход к пункту Menu
        WebElement menu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Menu']")));
        menu.click();

        // Наведение на Main Item 2
        WebElement menuItemMiddle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Main Item 2']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", menuItemMiddle);
        Actions actions = new Actions(driver);
        actions.moveToElement(menuItemMiddle).build().perform();

        // Ожидание появления SUB SUB LIST
        WebElement subSubList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='SUB SUB LIST »']")));
        actions.moveToElement(subSubList).build().perform();

        // Проверка количества элементов Sub Sub Item
        List<WebElement> subListItem = driver.findElements(By.xpath("//a[contains(text(),'Sub Sub Item')]"));
        Assertions.assertEquals(2, subListItem.size());
    }
}
