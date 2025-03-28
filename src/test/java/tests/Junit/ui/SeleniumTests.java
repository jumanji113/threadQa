package tests.Junit.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class SeleniumTests {

    private WebDriver driver;
    @BeforeEach
    public void setUp(){
        driver = new ChromeDriver();
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        // Отключаем энергосберегающие функции
        //chromeOptions.addArguments("--disable-backgrounding-occluded-windows");
        //chromeOptions.addArguments("--disable-background-timer-throttling");
        //chromeOptions.addArguments("--disable-renderer-backgrounding");
        driver.manage().window().setSize(new Dimension(1920, 1080));
        //driver.navigate().refresh();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterEach
    public void tearDown(){
        driver.close();
    }
    @Test
    public void simpleUiTest() {
        driver.get("https://threadqa.ru/");
        String actualTile = driver.getTitle();
        String expectedTtitle = "Олег Пендрак - Инженер по автоматизации тестирования QA Automation";
        Assertions.assertEquals(actualTile, expectedTtitle);
    }

    @Test
    public void simpleFormTest(){
        String expectedName = "Thomas Anderson";
        String expectedEmail = "thomasAnderson@mail.ru";
        String expectedCurrentAddress = "Moscow Lenina 41";
        String expectedPermanentAddress = "Usa Los Angeles";

        driver.get("http://85.192.34.140:8081");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text() = 'Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text() = 'Text Box']"));
        elementsTextBox.click();

        WebElement elementsFullName = driver.findElement(By.id("userName"));
        WebElement elementsEmail = driver.findElement(By.id("userEmail"));
        WebElement elementsCurrentAdress = driver.findElement(By.id("currentAddress"));
        WebElement elementsPermanentAdress = driver.findElement(By.id("permanentAddress"));
        WebElement elementsSubmit = driver.findElement(By.id("submit"));

        elementsFullName.sendKeys(expectedName);
        elementsEmail.sendKeys(expectedEmail);
        elementsCurrentAdress.sendKeys(expectedCurrentAddress);
        elementsPermanentAdress.sendKeys(expectedPermanentAddress);
        elementsSubmit.click();

        WebElement nameNew = driver.findElement(By.id("name"));
        WebElement emailNew = driver.findElement(By.id("email"));
        WebElement currentAddressNew = driver.findElement(By.xpath("//div[@id = 'output']//p[@id='currentAddress']"));
        WebElement permanentAddressNew = driver.findElement(By.xpath("//div[@id = 'output']//p[@id='permanentAddress']"));

        String actualName = nameNew.getText();
        String actualEmail = emailNew.getText();
        String actualCurrentAddress = currentAddressNew.getText();
        String actualPermanentAddress = permanentAddressNew.getText();

        Assertions.assertTrue(actualName.contains(expectedName));
        Assertions.assertTrue(actualEmail.contains(expectedEmail));
        Assertions.assertTrue(actualCurrentAddress.contains(expectedCurrentAddress));
        Assertions.assertTrue(actualPermanentAddress.contains(expectedPermanentAddress));
    }

    @Test
    public void uploadAndDownloadTest(){
        driver.get("http://85.192.34.140:8081");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text() = 'Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text() = 'Upload and Download']"));
        elementsTextBox.click();

        WebElement uploadFileButton = driver.findElement(By.xpath("//input[@id= 'uploadFile']"));
        WebElement downloadFileButton = driver.findElement(By.xpath("//a[@id= 'downloadButton']"));
    }
}