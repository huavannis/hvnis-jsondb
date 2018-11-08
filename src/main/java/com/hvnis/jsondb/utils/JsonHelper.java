package com.hvnis.jsondb.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author hvnis
 */
public final class JsonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     */
    private JsonHelper() {
        
    }

    public static String parseToString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
    
    public static <T extends Serializable> T parseToObject(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }
}
