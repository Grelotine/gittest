package org.squashtest.tf.gittest.service;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SourceCodeManagementService {

    public static final Logger LOGGER = LoggerFactory.getLogger(SourceCodeManagementService.class);

    private static final String DOT_GIT = ".git";
    private static final String COMMITTER_NAME = "SquashTM";
    private static final String COMMIT_MESSAGE = "Add ";
    private static final String REVERT_COMMIT_MESSAGE = "Revert modifications in feature folder.";

    private boolean shallPass = true;

    public void publishTestCaseToGitOnGivenBranch(
            String localRepositoryPath, String remoteRepositoryUrl,
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
        gitHelper.resetAndPullBranch();
        // 4 - Manage the potential modifications in feature folder by strangers
        managePotentialExteriorModifications(gitHelper, featureFolderRelativePath);
        // 5 - Create the .feature file in the feature folder
        if(shallPass) {
            String fileRelativePath = FileHelper.createFeatureFile(localRepositoryPath, featureFolderRelativePath, testCaseName, script);
            // Add the file to the current index
            gitHelper.addFileToIndex(fileRelativePath);
            // Commit the index
            gitHelper.commit(COMMITTER_NAME, COMMIT_MESSAGE + testCaseName);
        // Push the commit
        gitHelper.safePush(username, password);
        }
        // END - Close the Resource
        gitHelper.closeResource();
    }

    /**
     * @deprecated : Apparently this method is no longer useful. The external modifications will be kept
     * until they are erased by new SquashTM modifications.<br/>
     * It will happen that another Author than SquashTM modify some files in the feature folder.
     * Since only SquashTM has the right to do this, all the modifications done on these files
     * will be reverted.
     * A simple backout is impossible because all other modifications has to be kept.
     * @param gitHelper
     * @param featureFolder
     */
    @Deprecated
    public void managePotentialExteriorModifications(GitManagementHelper gitHelper, String featureFolder) {
        Iterable<RevCommit> logs = gitHelper.log();
        // Getting the last commit of SquashTM
        RevCommit lastSquashTMCommit = gitHelper.getLastCommitOfAuthor(COMMITTER_NAME, logs);
        // If exists, find all modified files since this last commit
        if(lastSquashTMCommit != null) {
            List<DiffEntry> diffEntries = gitHelper.diffCommitToHead(lastSquashTMCommit);
            if(!diffEntries.isEmpty()) {
                List<String> filesToRevert = findExternallyModifiedFiles(diffEntries, featureFolder);
                if(!filesToRevert.isEmpty()) {
                    gitHelper.revertFilesToACommit(filesToRevert, lastSquashTMCommit);
                    gitHelper.addFilesToIndex(filesToRevert);
                    gitHelper.commit(COMMITTER_NAME, REVERT_COMMIT_MESSAGE);
                } else {
                    LOGGER.info("No modifications were made in the feature folder since last commit of SquashTM.");
                }
            } else {
                LOGGER.info("No modifications were made since last commit of SquashTM.");
            }
        } else {
            LOGGER.info("SquashTM has never committed on this Git Branch.");
        }
    }

    /**
     * @deprecated : Apparently this method is no longer useful. The external modifications will be kept
     * until they are erased by new SquashTM modifications.<br/>
     * From a List of {@link DiffEntry}, find the files that were modified in the feature folder but not by SquashTM. */
    @Deprecated
    private List<String> findExternallyModifiedFiles(List<DiffEntry> diffEntries, String featureFolder) {
        List<String> modifiedFiles = new ArrayList<>();
        LOGGER.info("Checking exterior modifications in folder: " + featureFolder);
        for(DiffEntry entry : diffEntries) {
            DiffEntry.ChangeType changeType = entry.getChangeType();
            String oldPath = entry.getOldPath();
            String newPath = entry.getNewPath();
            // Is the modified file in the feature folder ?
            if(FileHelper.isFileInFolder(newPath, featureFolder)) {
                    // Means the file has either been added, copied, modified or renamed
                        LOGGER.info("The file " + newPath + " was added/copied/modified/renamed but not by SquashTM.");
                        modifiedFiles.add(newPath);
            } else if(FileHelper.isFileInFolder(oldPath, featureFolder)) {
                // Means the file has been deleted
                        LOGGER.info("The file " + oldPath + " was deleted but not by SquashTM.");
                        modifiedFiles.add(oldPath);
            }
            LOGGER.trace("DiffEntry: OldPath: " + entry.getOldPath()+ " NewPath: " + entry.getNewPath() + " ChangedType: " + changeType);
        }
        return modifiedFiles;
    }
}