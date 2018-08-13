package com.seam.messages.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utils class to map json to object
 */
public class MapperUtils {

    private static ObjectMapper objectMapper;

    public static ObjectMapper getMapper() {

        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;

    }

}
