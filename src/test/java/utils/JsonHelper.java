package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T fromJson(String jsonPath, Class<T> out) throws IOException{
            return mapper.readValue(new File(jsonPath), out);
    }
    public static <T> T fromJsonString(String json, Class<T> out) throws IOException{
            return mapper.readValue(json, out);
    }


    public static String toJson(Object objectClass) throws JsonProcessingException{
            return mapper.writeValueAsString(objectClass);
    }
}