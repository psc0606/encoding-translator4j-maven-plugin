package org.sogou.encoding.detector;

import com.ibm.icu.text.CharsetDetector;
import org.sogou.encoding.util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * http://userguide.icu-project.org/conversion/detection
 *
 * @author pengshaocheng 2020/3/16
 */
public class Icu4jDetector implements EncodingDetector {
    @Override
    public String detect(File file) throws IOException {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(FileUtil.readBytes(file));
        return detector.detect().getName();
    }
}
