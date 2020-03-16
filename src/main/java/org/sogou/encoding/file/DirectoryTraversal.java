package org.sogou.encoding.file;

import org.apache.maven.plugin.logging.Log;
import org.sogou.encoding.Callback;

import java.io.File;

/**
 * @author pengshaocheng 2020/3/16
 */
public class DirectoryTraversal {
    private static Log LOG;

    public DirectoryTraversal(Log log) {
        LOG = log;
    }

    public void traverse(String rootPath, Callback callback) {
        File rootFile = new File(rootPath);
        if (!rootFile.isDirectory()) {
            LOG.info("The root path is not directory: " + rootPath);
            return;
        }
        doTraverse(rootFile, 0, -1, callback);
    }

    /**
     * Traverse all files within the root path recursively.
     *
     * @param rootPath     root path
     * @param currentDepth current depth from root path.
     * @param depth        -1 means recursively infinite, 0 means only traverses root path files, etc.
     * @param callback     callback action
     */
    private void doTraverse(File rootPath, int currentDepth, int depth, Callback callback) {
        // not -1 and the traverse depth lgt depth.
        if (depth != -1 && currentDepth >= depth) {
            return;
        }
        File[] subFiles = rootPath.listFiles();
        if (subFiles == null) {
            return;
        }
        for (File f : subFiles) {
            if (f.isFile()) {
                try {
                    callback.action(f);
                } catch (Exception e) {
                    LOG.error(e);
                }
            } else {
                doTraverse(f, currentDepth + 1, depth, callback);
            }
        }
    }
}
