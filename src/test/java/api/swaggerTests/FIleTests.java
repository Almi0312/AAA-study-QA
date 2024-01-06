package api.swaggerTests;

import api.listener.CustomTpl;
import api.service.FileService;
import api.service.UserService;
import io.qameta.allure.Attachment;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Random;

import static api.assertion.Conditions.hasMessage;
import static api.assertion.Conditions.hasStatusCode;

public class FIleTests {
    private static FileService fileService;
    @BeforeAll
    public static void setUp() {
        fileService = new FileService();
        RestAssured.baseURI = "http://85.192.34.140:8080/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(),
                CustomTpl.customLogFilter().withCustomTemplates());
    }

    @Test
    public void positiveDownloadTest(){
        byte[] file = fileService.downloadBaseImage()
                .asResponse().asByteArray();
        attachFile(file);
        File expectedFile = new File("src/test/resources/threadqa.jpeg");

        Assertions.assertEquals(expectedFile.length(), file.length);
    }

    /**
     * Cлужит для того, что бы в allure отчете отображалось изображение
     * которое использовалось
     * @param bytes переданный файл в байтах
     */
    @Attachment(value = "downloaded", type = "image/png")
    private byte[] attachFile(byte[] bytes){
        return bytes;
    }

    @Test
    public void positiveUploadTest(){
        File expectedFile = new File("src/test/resources/threadqa.jpeg");
        fileService.uploadFile(expectedFile)
                .should(hasStatusCode(200))
                .should(hasMessage("file uploaded to server"));

        byte[] actualFile = fileService.downloadLastImage()
                .asResponse().asByteArray();

        Assertions.assertTrue(actualFile.length != 0);
        Assertions.assertEquals(expectedFile.length(), expectedFile.length());
    }
}
