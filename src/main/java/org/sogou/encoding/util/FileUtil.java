package org.sogou.encoding.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author pengshaocheng 2020/3/16
 */
public class FileUtil {
    public static byte[] readBytes(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        return Files.readAllBytes(path);
    }
}
