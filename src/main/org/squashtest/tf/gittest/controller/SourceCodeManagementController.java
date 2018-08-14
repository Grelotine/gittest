package org.squashtest.tf.gittest.controller;

import org.squashtest.tf.gittest.service.SourceCodeManagementService;

public class SourceCodeManagementController {

        private SourceCodeManagementService scmService = new SourceCodeManagementService();


    /** Only managing Basic Authentication yet. */
    public void publishTestCaseToGit(String localRepositoryPath, String remoteRepositoryPath,
                                     String testBranchName, boolean createBranchIfNotExist,
                                     String featureFolderRelativePath,
                                     String testCaseName, String script,
                                     String username, String password) {

        scmService.publishTestCaseToGitOnGivenBranch(
                localRepositoryPath, remoteRepositoryPath,
                testBranchName, createBranchIfNotExist,
                featureFolderRelativePath, testCaseName, script,
                username, password);
    }
}
