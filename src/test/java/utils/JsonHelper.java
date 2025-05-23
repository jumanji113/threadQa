package utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;

public class JsonHelper {
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T fromJson(String jsonPath, Class<T> out)  {
            return mapper.readValue(new File(jsonPath), out);

    }

    @SneakyThrows
    public static <T> T fromString(String jsonPath, Class<T> out)  {
        return mapper.readValue(jsonPath, out);

    }

    @SneakyThrows
    public static String jsonFromObj(Object object){
            return mapper.writeValueAsString(object);
    }
}
