package org.squashtest.tf.gittest.exception;

public class NoSuchFolderException extends RuntimeException {

    private static final String buildMessage(String repositoryPath) {
        return "The folder with Path '" + repositoryPath + "' does not exist.";
    }

    public NoSuchFolderException(String repositoryPath) {
        super(buildMessage(repositoryPath));
    }
}
