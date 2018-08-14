package org.squashtest.tf.gittest.domain;

/**
 * POJO representing the Git settings for a given Squash Project.
 */
public class GitProjectSettings {

    private String localRepositoryPath;
    private String remoteRepositoryPath;
    private String targetBranchName;
    private boolean createBranchIfNotExist;
    private String featureFolderRelativePath;
    private String gitUsername;
    private String gitPassword;

    public GitProjectSettings(String localRepositoryPath, String remoteRepositoryPath,
                              String targetBranchName, boolean createBranchIfNotExist,
                              String featureFolderRelativePath,
                              String gitUsername, String gitPassword) {
        this.localRepositoryPath = localRepositoryPath;
        this.remoteRepositoryPath = remoteRepositoryPath;
        this.targetBranchName = targetBranchName;
        this.createBranchIfNotExist = createBranchIfNotExist;
        this.featureFolderRelativePath = featureFolderRelativePath;
        this.gitUsername = gitUsername;
        this.gitPassword = gitPassword;
    }

    public String getLocalRepositoryPath() {
        return localRepositoryPath;
    }

    public void setLocalRepositoryPath(String localRepositoryPath) {
        this.localRepositoryPath = localRepositoryPath;
    }

    public String getRemoteRepositoryPath() {
        return remoteRepositoryPath;
    }

    public void setRemoteRepositoryPath(String remoteRepositoryPath) {
        this.remoteRepositoryPath = remoteRepositoryPath;
    }

    public String getTargetBranchName() {
        return targetBranchName;
    }

    public void setTargetBranchName(String targetBranchName) {
        this.targetBranchName = targetBranchName;
    }

    public boolean isCreateBranchIfNotExist() {
        return createBranchIfNotExist;
    }

    public void setCreateBranchIfNotExist(boolean createBranchIfNotExist) {
        this.createBranchIfNotExist = createBranchIfNotExist;
    }

    public String getFeatureFolderRelativePath() {
        return featureFolderRelativePath;
    }

    public void setFeatureFolderRelativePath(String featureFolderRelativePath) {
        this.featureFolderRelativePath = featureFolderRelativePath;
    }

    public String getGitUsername() {
        return gitUsername;
    }

    public void setGitUsername(String gitUsername) {
        this.gitUsername = gitUsername;
    }

    public String getGitPassword() {
        return gitPassword;
    }

    public void setGitPassword(String gitPassword) {
        this.gitPassword = gitPassword;
    }
}
