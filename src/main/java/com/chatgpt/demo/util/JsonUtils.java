package com.chatgpt.demo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.StringWriter;
import java.io.Writer;

/**
 * Json工具类
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper objectmapper;

    static {
        objectmapper = new ObjectMapper();

        // ignore unknow properties
        objectmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // ignore null field
        objectmapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 转json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            Writer strWriter = new StringWriter();
            objectmapper.writeValue(strWriter, object);
            return strWriter.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 转java对象
     *
     * @param jsonSource
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T toJavaObject(String jsonSource, Class<T> valueType) {
        try {
            if (StringUtils.isBlank(jsonSource)) {
                return null;
            }
            return objectmapper.readValue(jsonSource, valueType);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
