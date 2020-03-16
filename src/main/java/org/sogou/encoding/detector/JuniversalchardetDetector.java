package org.sogou.encoding.detector;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.File;
import java.io.IOException;

/**
 * DO NOT USE, only support partial Chinese code. It will detect wrong encoding for Chinese.
 *
 * https://github.com/albfernandez/juniversalchardet
 *
 * @author pengshaocheng 2020/3/16
 */
public class JuniversalchardetDetector implements EncodingDetector {
    @Override
    public String detect(File file) throws IOException {
        return UniversalDetector.detectCharset(file);
    }
}
