package org.squashtest.tf.gittest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * This class offers some methods to manage .feature Files creation.
 */
public class FileHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    private static final String DOT_FEATURE = ".feature";

    public static boolean isFileExist(String path) {
        return Files.exists(Paths.get(path));
    }

    public static String createFeatureFile(String localRepositoryPath, String featureFolderRelativePath, String testCaseName, String script) {
        List<String> lines = Arrays.asList("Header", script, "Footer");
        String fileRelativePath = buildRelativePath(featureFolderRelativePath, testCaseName);
        String fileAbsolutePath = buildAbsolutePath(localRepositoryPath, fileRelativePath);
        Path filePath = Paths.get(fileAbsolutePath);
        try {
            Files.write(filePath, lines, Charset.forName("UTF8"));
        } catch(IOException ioe) {
            LOGGER.error("Error while writing the file with path " + fileAbsolutePath, ioe);
        }
        return fileRelativePath;
    }

    private static String buildRelativePath(String folderRelativePath, String testCaseName) {
        return folderRelativePath + "/" + testCaseName + DOT_FEATURE;
    }

    private static String buildAbsolutePath(String localRepositoryPath, String fileRelativePath) {
        return localRepositoryPath + "/" + fileRelativePath;
    }
}
