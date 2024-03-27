package com.camping.server.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper MAPPER = new ObjectMapper();

    public static String write(Object object){
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("invalid json",e);
            return null;
        }
    }

    public static <T> T read(String string,Class<T> clazz){
        try {
            return MAPPER.readValue(string,clazz);
        } catch (JsonProcessingException e) {
            logger.error("invalid json",e);
            return null;
        }
    }
}
