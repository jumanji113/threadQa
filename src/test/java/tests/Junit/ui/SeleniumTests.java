package tests.Junit.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SeleniumTests {

    private String downloadFolder = System.getProperty("user.dir") + File.separator + "build" + File.separator + "downloadFiles";
    private WebDriver driver;

    @BeforeAll
    public static void downloadChromeDriver(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        Map<String, String> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadFolder);
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        // Отключаем энергосберегающие функции
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-renderer-backgrounding");
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
    public void uploadFileTest(){
        driver.get("http://85.192.34.140:8081");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text() = 'Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text() = 'Upload and Download']"));
        elementsTextBox.click();

        WebElement uploadFileButton = driver.findElement(By.xpath("//input[@id= 'uploadFile']"));
        uploadFileButton.sendKeys((System.getProperty("user.dir")) + "/src/test/resources/threadqa.jpeg");

        WebElement uploadedFilePath = driver.findElement(By.xpath("//p[@id = 'uploadedFilePath']"));
        Assertions.assertTrue(uploadedFilePath.getText().contains("threadqa.jpeg"));
    }

    @Test
    public void downloadFileTest(){
        driver.get("http://85.192.34.140:8081");
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text() = 'Elements']"));
        elementsCard.click();
        WebElement elementsTextBox = driver.findElement(By.xpath("//span[text() = 'Upload and Download']"));
        elementsTextBox.click();

        WebElement downloadFileButton = driver.findElement(By.xpath("//a[@id= 'downloadButton']"));
        downloadFileButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(x-> Paths.get(downloadFolder, "sticker.png").toFile().exists());


        File file = new File("build/downloadFiles/sticker.png");
        Assertions.assertTrue(file.length() != 0);
        Assertions.assertNotNull(file);
    }
}