package org.squashtest.tf.gittest.exception;

public class GitRepositoryInitializingException extends RuntimeException {

    private static final String buildMessage(String repositoryPath, String additionalMessage) {
        return "An error appeared while initializing the Git Repository at path: '" + repositoryPath + "'. " + additionalMessage;
    }

    public GitRepositoryInitializingException(String repositoryPath, String additionalMessage) {
        super(buildMessage(repositoryPath, additionalMessage));
    }
}
