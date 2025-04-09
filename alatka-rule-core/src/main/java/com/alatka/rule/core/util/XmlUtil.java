package com.alatka.rule.core.util;

import com.alatka.rule.core.support.FileWrapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;

import java.io.IOException;
import java.util.Map;

public class XmlUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new XmlFactory());

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> Map<String, T> getMap(FileWrapper file, Class<T> clazz) {
        return getMap(file, null, clazz);
    }

    public static <T> Map<String, T> getMap(FileWrapper file, String rootName, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readerForMapOf(clazz).withRootName(rootName).readValue(file.getContent());
        } catch (IOException e) {
            throw new RuntimeException("获取xml文件错误: " + file.getName(), e);
        }
    }
}
