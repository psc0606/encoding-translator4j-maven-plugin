package org.sogou.encoding;

import java.io.File;

/**
 * @author pengshaocheng 2020/3/16
 */
public interface Callback {
    void action(File file) throws Exception;
}
