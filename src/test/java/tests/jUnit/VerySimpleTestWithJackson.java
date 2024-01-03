package tests.jUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.People;
import org.junit.jupiter.api.Test;
import utils.JsonHelper;

import java.io.File;
import java.io.IOException;

public class VerySimpleTestWithJackson {

    @Test
    public void simpleTest() throws Exception{
        People people = JsonHelper.fromJson(("src/test/resources/stas.json"), People.class);
        System.out.printf("%s\n%s\n%s\n", people.getName(), people.getAge(), people.getSex());

        People sasha = new People("sasha", 10, "female");
        String json = JsonHelper.toJson(sasha);
        System.out.println(json);
        System.out.println(JsonHelper.toJson(people));
    }
    
}
