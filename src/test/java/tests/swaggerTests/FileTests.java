package tests.swaggerTests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import listener.CustomTpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.FileService;
import services.UserService;

import java.io.File;
import java.util.Random;

import static assertions.Conditions.haseMessage;
import static assertions.Conditions.haseStatus;

public class FileTests {
    private static FileService fileService;
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
        fileService = new FileService();
    }

    @Test
    public void fileDownload(){
        byte[] file = fileService.downloadImage()
                .asResponse().asByteArray();
        File expectedFile = new File("src/test/resources/threadqa.jpeg");

        Assertions.assertEquals(expectedFile.length(), file.length);
    }

    @Test
    public void PostiveUploadTest(){
        File expectedFile = new File("src/test/resources/threadqa.jpeg");
        fileService.uploadFile(expectedFile).should(haseStatus(200))
                .should(haseMessage("file uploaded to server"));

        byte[] actualFile = fileService.donwloadLastUploaded().asResponse().asByteArray();
        Assertions.assertTrue(actualFile.length != 0);
        Assertions.assertEquals(expectedFile.length(), actualFile.length);
    }


}
