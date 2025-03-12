package tests.Junit;

import lombok.SneakyThrows;
import models.Settings;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.AppConfig;
import utils.JsonHelper;

import java.io.FileInputStream;
import java.util.Properties;

@Tag("UNIT")
public class PropertiesReaderTest {

    @Test
    @SneakyThrows
    @Tag("SMOKE")
    public void simpleReader() {
        Properties properties = new Properties();
        FileInputStream fs = new FileInputStream("src/test/resources/project.properties");
        properties.load(fs);

        String url = properties.getProperty("url");
        boolean isProduction = Boolean.parseBoolean(properties.getProperty("is_production"));
        int threads = Integer.parseInt(properties.getProperty("threads"));
        Assertions.assertEquals(url, "https://google");
        Assertions.assertEquals(isProduction, false);
        Assertions.assertEquals(3, threads);
    }

    @Test
    @SneakyThrows
    public void jacksonReader(){
        Properties properties = new Properties();
        FileInputStream fs = new FileInputStream("src/test/resources/project.properties");
        properties.load(fs);
        String json = JsonHelper.jsonFromObj(properties);
        System.out.println(json);
        Settings settings = JsonHelper.fromString(json, Settings.class);
        System.out.println(settings);
        System.out.println(settings.getUrl());
        System.out.println(settings.getThreads());
        System.out.println(settings.isProduction());
    }

    @Test
    @SneakyThrows
    public void ownerReaderText(){
        AppConfig appConfig = ConfigFactory.create(AppConfig.class);
        System.out.println(appConfig);
    }
}
