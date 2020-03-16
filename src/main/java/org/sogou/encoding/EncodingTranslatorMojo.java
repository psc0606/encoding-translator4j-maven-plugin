package org.sogou.encoding;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.sogou.encoding.detector.*;
import org.sogou.encoding.file.DirectoryTraversal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * You can refer to apache doc.
 * <a>http://maven.apache.org/plugin-developers/index.html</a>
 *
 * @author pengshaocheng 2020/3/15
 */
@Mojo(name = "translator4j", defaultPhase = LifecyclePhase.COMPILE)
public class EncodingTranslatorMojo extends AbstractMojo {
    /**
     * Whether to detect file encoding automatically.
     * The property means it can be defined in command line by `-D`, such as -Dtranslator4j.autoDetect=false
     */
    @Parameter(property = "translator4j.autoDetect", defaultValue = "true")
    private boolean autoDetect;

    /**
     * The original file encoding.
     */
    @Parameter(property = "translator4j.originalEncoding", defaultValue = "UTF-8")
    private String originalEncoding;

    /**
     * Whether to skip unknown file encoding
     */
    @Parameter(property = "translator4j.skipUnknownFileEncoding", defaultValue = "true")
    private boolean skipUnknownFileEncoding;

    /**
     * The target encoding such as: UTF-8
     */
    @Parameter(required = false, property = "translator4j.encoding", defaultValue = "UTF-8")
    private String encoding;

    /**
     * Auto inject source directory from maven variable ${project.build.sourceDirectory}
     */
    @Parameter(readonly = true, defaultValue = "${project.build.sourceDirectory}")
    private String sourceDirectory;

    /**
     * Auto inject test source directory from maven variable ${project.build.sourceDirectory}
     */
    @Parameter(readonly = true, defaultValue = "${project.build.testSourceDirectory}")
    private String testSourceDirectory;

    @Parameter(readonly = true, defaultValue = "${project.build.resources[0].directory}")
    private String resourceDirectory;

//    Also
//    @Parameter(readonly = true, defaultValue = "${project.build.resources}")
//    List<Resource> resourceList;

    @Parameter(readonly = true, defaultValue = "${project.build.testResources[0].directory}")
    private String testResourceDirectory;

    @Parameter(readonly = true, defaultValue = "juniversalchardet")
    private String encodingDetector;

    private final Map<String, EncodingDetector> encodingDetectorMap = new HashMap<>();

    public EncodingTranslatorMojo() {
        encodingDetectorMap.put("juniversalchardet", new JuniversalchardetDetector());
        encodingDetectorMap.put("jchardet", new JchardetDetector());
        encodingDetectorMap.put("icu4j", new Icu4jDetector());
        encodingDetectorMap.put("cpdetector", new CpdetectorDetector());
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Translator4j now start detect java and resource file encoding...");
        getLog().info("Translator4j source:" + sourceDirectory);
        getLog().info("Translator4j test source:" + testSourceDirectory);
        getLog().info("Translator4j resource:" + resourceDirectory);
        getLog().info("Translator4j test resource:" + testResourceDirectory);

        DirectoryTraversal traversal = new DirectoryTraversal(getLog());
        final EncodingDetector detector = encodingDetectorMap.get(encodingDetector);
        if (detector == null) {
            throw new MojoExecutionException("Not support encoding detector: " + encodingDetector);
        }
        traversal.traverse(sourceDirectory, new Callback() {
            @Override
            public void action(File file) throws Exception {
                String detectEncoding = detector.detect(file);
                if (skipUnknownFileEncoding) {
                    if (detectEncoding == null) {
                        getLog().info("Skip unknown encoding file: " + file.getName());
                    } else {
                        getLog().info("file:" + file.getName() + ":" + detectEncoding);
                        if (detectEncoding.equalsIgnoreCase(encoding)) {
                            return;
                        }
                        saveWithEncoding(file, detectEncoding);
                    }
                } else {
                    throw new MojoExecutionException("the encoding of " + file + "is null.");
                }
            }
        });
    }

    private void saveWithEncoding(File file, String detectEncoding) throws IOException {
        Path path = Paths.get(file.getPath());
        byte[] bytes = Files.readAllBytes(path);
        String content = new String(bytes, Charset.forName(detectEncoding));
        System.out.println(content);
        Files.write(path, content.getBytes(encoding));
    }
}
