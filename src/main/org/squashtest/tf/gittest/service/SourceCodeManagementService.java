package org.squashtest.tf.gittest.service;

import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceCodeManagementService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SourceCodeManagementService.class);

    private static final String DOT_GIT = ".git";
    private static final String COMMITTER_NAME = "SquashTM";
    private static final String COMMIT_MESSAGE = "Add ";

    private boolean pass = false;

    public void publishTestCaseToGitOnGivenBranch(
            String localRepositoryPath, String remoteRepositoryPath,
            String testBranchName, boolean createBranchIfNotExist,
            String featureFolderRelativePath, String testCaseName, String script,
            String username, String password) {

        LOGGER.info("Publishing Gherkin script of TestCase '" + testCaseName + "' to git branch '" + testBranchName + "'.");
        // 1 - Get the .git repository.
        GitManagementHelper gitHelper = new GitManagementHelper(localRepositoryPath + DOT_GIT);
        // 2 - Switch to the right Branch.
        gitHelper.manageBranchSwitching(testBranchName, createBranchIfNotExist);
        // 3 - Reset and update the branch -
        // Erase all uncommited modifications, unpushed commits and pull the remote branch.
        gitHelper.resetAndUpdateLocalBranch();
        // 4 - Pull to update de HEAD
        // gitHelper.pull();
        // Create the .feature file in the feature folder
        if(pass) {
            String fileRelativePath = FileHelper.createFeatureFile(localRepositoryPath, featureFolderRelativePath, testCaseName, script);
            // Add the file to the current index
            gitHelper.addFileToIndex(fileRelativePath);
            // Commit the index
            gitHelper.commit(COMMITTER_NAME, COMMIT_MESSAGE + testCaseName);
            // Push the commit
            gitHelper.push(username, password);
        }

        for(RevCommit rev : gitHelper.log()) {
            LOGGER.info("AuthorIdent: " + rev.getAuthorIdent().getName());
        }
        gitHelper.closeResource();
    }

}
