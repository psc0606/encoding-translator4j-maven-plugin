package org.sogou.encoding.detector;

import java.io.File;
import java.io.IOException;

/**
 * @author pengshaocheng 2020/3/16
 */
public interface EncodingDetector {
    /**
     * File encoding detect.
     *
     * @return file encoding
     */
    String detect(File file) throws IOException;
}
