package org.squashtest.tf.gittest.exception;

public class NotGitRepositoryException extends RuntimeException {

    private static final String buildMessage(String repositoryPath) {
        return "The folder '" + repositoryPath + "' is not a Git Repository.";
    }

    public NotGitRepositoryException(String repositoryPath) {
        super(buildMessage(repositoryPath));
    }
}
