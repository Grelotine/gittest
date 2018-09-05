package org.squashtest.tf.gittest.exception;

/** Exception thrown when trying to switch to a Git Branch that doesn't exist while it should exist. */
public class GitBranchDoesNotExistException extends RuntimeException {

    private static final String buildMessage(String branchName) {
        return "The given Git Branch with name '" + branchName + "' does not exist.";
    }

    private String givenGitBranchName;

    public GitBranchDoesNotExistException(String givenGitBranchName) {
        super(buildMessage(givenGitBranchName));
        this.givenGitBranchName = givenGitBranchName;
    }

    public String getGivenGitBranchName() {
        return givenGitBranchName;
    }
    public void setGivenGitBranchName(String givenGitBranchName) {
        this.givenGitBranchName = givenGitBranchName;
    }
}
