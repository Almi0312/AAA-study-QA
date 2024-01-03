package tests.jUnit;

import models.Settings;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.AppConfig;
import utils.JsonHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Tag("UNIT")
public class PropertiesReaderTest {
    @Test
    @Tag("SMOKE") //помечаем тег
    public void simpleReaderTest() throws IOException {
        Properties properties = new Properties();
        try(FileInputStream inputStream = new FileInputStream("src/test/resources/project.properties")){
            properties.load(inputStream);
        }

        String url = properties.getProperty("url");
        boolean isProduction = Boolean.parseBoolean(properties.getProperty("is.production"));

        Assertions.assertEquals("https://google.ru", url);
        Assertions.assertTrue(isProduction);
    }

    @Test
    public void jacksonTest() throws IOException{
        Properties properties = new Properties();
        try(FileInputStream inputStream = new FileInputStream("src/test/resources/project.properties")){
            properties.load(inputStream);
        }

        String json = JsonHelper.toJson(properties);
        System.out.println(json);

        Settings settings = JsonHelper.fromJsonString(json, Settings.class);
        System.out.println(settings.getIsProduction());
        System.out.println(settings.getUrl());
    }

    @Test
    public void ownerReaderTest(){
        AppConfig config = ConfigFactory.create(AppConfig.class);
        System.out.println(config.url());
        System.out.println(config.isProd());
    }
}
