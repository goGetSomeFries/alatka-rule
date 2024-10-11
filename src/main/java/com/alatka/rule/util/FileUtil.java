package com.alatka.rule.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 *
 * @author ybliu
 */
public class FileUtil {

    public static List<Path> getClasspathFiles(String classpath, String suffix) {
        return getClasspathFiles(classpath, suffix, FileUtil.class.getClassLoader());
    }

    public static List<Path> getClasspathFiles(String classpath, String suffix, ClassLoader classLoader) {
        try {
            URL url = classLoader.getResource(classpath);
            if (url == null) {
                throw new IllegalArgumentException("can not find classpath: " + classpath);
            }

            List<Path> list = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(url.toURI()), suffix)) {
                stream.forEach(list::add);
            }
            return list;
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFileContent(String classpath) {
        try {
            Path path = Paths.get(classpath);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}