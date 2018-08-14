package org.squashtest.tf.gittest.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.squashtest.tf.gittest.exception.GitBranchDoesNotExistException;
import org.squashtest.tf.gittest.exception.NoSuchFolderException;
import org.squashtest.tf.gittest.exception.NotGitRepositoryException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class helps to use JGit plugin and offers the commonly used Commands.
 * */
public class GitManagementHelper {

    public static final Logger LOGGER = LoggerFactory.getLogger(GitManagementHelper.class);

    private static final String NOT_GIT_REPOSITORY_ERROR_MESSAGE = "The given Repository is not a Git Repository: ";
    private static final String ACCESS_REPOSITORY_ERROR_MESSAGE = "Error while accessing repository with path: ";
    private static final String GETTING_CURRENT_BRANCH_NAME_ERROR_MESSAGE = "Error while getting current Git Branch name";
    private static final String FETCH_LOCAL_BRANCHES_LIST_ERROR_MESSAGE = "Could not retrieve branch list from local repository.";
    private static final String FETCH_ALL_BRANCHES_LIST_ERROR_MESSAGE = "Could not retrieve branch list from local and remote repository.";
    private static final String CREATE_BRANCH_EXCEPTION = "Could not create the Branch with name: ";
    private static final String CHECKOUT_BRANCH_EXCEPTION = "Error while switching to Branch with name: ";
    private static final String ADD_ERROR_MESSAGE = "Error while adding file to Index.";
    private static final String PUSH_ERROR_MESSAGE = "Error while pushing commits to Remote Repository. " +
                    "Make sure you have all the permissions with the given Credentials.";

    private FileRepository localRepository;
    private FileRepository remoteRepository;

    private Git git;

    public GitManagementHelper(String localRepositoryPathParam) {
        /* TODO: Cette méthode n'appartient-elle pas à un cas métier ?
        * Auquel cas on devrait peut-être l'appeler de l'extérieur. */
        localRepository = findLocalRepositoryOrCreateIt(localRepositoryPathParam);
        git = new Git(localRepository);
    }

    /** Clean the local working repository. */
    public void cleanLocalWorkingRepository() {
        try {
            LOGGER.info("Cleaning the local working repository.");
            git.clean().setForce(true).call();
        } catch(GitAPIException ex) {
            LOGGER.error("Error while cleaning the current working repository.", ex);
        }
    }

    /** Reset and update the local Branch.
     * Delete all uncommitted modifications, all unpushed commits, and pull remote branch. */
    public void resetAndUpdateLocalBranch() {
        BranchTrackingStatus status = getBranchTrackingStatus();
        try {
            LOGGER.info("Resetting the local working repository.");
            git.reset().setRef(status.getRemoteTrackingBranch()).setMode(ResetCommand.ResetType.HARD).call();
        } catch (GitAPIException ex) {
            LOGGER.error("Error while resetting the current working repository.", ex);
        }
    }

    /**
     * Counts the number of commits ahead and behind compared to the remote branch on the given branch.
     */
    public BranchTrackingStatus getBranchTrackingStatus() {
        BranchTrackingStatus status = null;
        try {
            Repository repository = git.getRepository();
            status = BranchTrackingStatus.of(repository, git.getRepository().getBranch());
        } catch (IOException ioex) {
            LOGGER.error("Error while getting BranchTrackingStatus.", ioex);
        } finally {
            return status;
        }
    }
    /** Creates the Git Branch with the given name.
     * @param branchNameParam The name of the Git Branch to create.
     */
    public void createBranch(String branchNameParam) {
        try {
            LOGGER.info("Creating branch with name '" + branchNameParam + "'.");
            git.branchCreate().setName(branchNameParam).call();
        } catch(GitAPIException ex) {
            LOGGER.error(CREATE_BRANCH_EXCEPTION + branchNameParam, ex);
        }
    }

    /** Switches to the Branch with the given name.
     * @param testBranchNameParam The name of the Branch to switch on.
     */
    public void switchBranch(String testBranchNameParam) {
        try {
            LOGGER.info("Checking out branch '" + testBranchNameParam + "'.");
            git.checkout().setName(testBranchNameParam).call();
        } catch(GitAPIException ex) {
            LOGGER.error(CHECKOUT_BRANCH_EXCEPTION + testBranchNameParam, ex);
        }
    }
    /**
     * Pulls modifications from remote reposiory.
     */
    public void pull() {
        LOGGER.info("Pulling remote repository.");
        try {
            git.pull().call();
        } catch (GitAPIException ex) {
            LOGGER.error("Error while pulling remote repository.");
        }
    }

    /** Adds the given file to the Git Index.
     * @param fileRelativePath The path to the file to add.
     */
    public void addFileToIndex(String fileRelativePath) {
        try {
            LOGGER.info("Adding file '" + fileRelativePath + "' to the index.");
            git.add().addFilepattern(fileRelativePath).call();
        } catch(GitAPIException ex) {
            LOGGER.error(ADD_ERROR_MESSAGE, ex);
        }
    }

    /** Commits the Git Index.
     * @param committerNameParam The name of the committer.
     * @param commitMessageParam The message of the commit. */
    public void commit(String committerNameParam, String commitMessageParam) {
        try {
            LOGGER.info("Committing with author '" + committerNameParam + "' and message '" + commitMessageParam + "'.");
            git.commit().setCommitter(committerNameParam, "").setMessage(commitMessageParam).call();
        } catch(GitAPIException ex) {
            LOGGER.error("Error during commit with commiter" + committerNameParam + " and message " + commitMessageParam, ex);
        }
    }

    /** Pushes the unpushed Git commits.
     * @param username The username to authenticate to the remote Git Repository.
     * @param password The password to authenticate to the remote Git Repository. */
    public void push(String username, String password) {
        try {
            LOGGER.info("Pushing commit with username '" + username + "'.");
            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)).call();
        } catch(TransportException tex) {
            LOGGER.error("TransportException: " + PUSH_ERROR_MESSAGE, tex);
        } catch(GitAPIException ex) {
            LOGGER.error("GitAPIException: " + PUSH_ERROR_MESSAGE, ex);
        } catch(Exception ex) {
            LOGGER.error("SimpleException: " + PUSH_ERROR_MESSAGE, ex);
        }
    }

    /** Finds the local Git Repository located at the given Path.
     * TODO: Manages only the finding, not the creation yet.
     * @param localRepositoryPath
     * @return
     */
    private FileRepository findLocalRepositoryOrCreateIt(String localRepositoryPath) {
        File folder = new File(localRepositoryPath);
        // 1 - Does the folder exist ?
        if(!FileHelper.isFileExist(localRepositoryPath)) {
            throw new NoSuchFolderException(localRepositoryPath);
        }
        // 2 - Is it a a real Git Repository ?
        if(!RepositoryCache.FileKey.isGitRepository(folder, FS.DETECTED)) {
            LOGGER.error(NOT_GIT_REPOSITORY_ERROR_MESSAGE + localRepositoryPath);
            throw new NotGitRepositoryException(localRepositoryPath);
        }
        LOGGER.info("Repository on path '" + localRepositoryPath + "' has been successfully identified.");
        FileRepository localRepository = null;
        try {
            localRepository = new FileRepository(localRepositoryPath);
        } catch(IOException ioException) {
            LOGGER.error(ACCESS_REPOSITORY_ERROR_MESSAGE + localRepositoryPath, ioException);
        } finally {
            return localRepository;
        }
    }

    /**
     * Get the branches List of the local Repository as a List of {@link Ref}.
     * @return
     */
    public List<Ref> getLocalBranchesList() {
        List<Ref> branches = new ArrayList<>();
        try {
            LOGGER.info("Listing branches.");
            branches = git.branchList().call();
        } catch (GitAPIException ex) {
            LOGGER.error(FETCH_LOCAL_BRANCHES_LIST_ERROR_MESSAGE, ex);
        }
        return branches;
    }

    /**
     * Get the branches List of the local and remote Repository as a List of {@link Ref}.
     * @return
     */
    public List<Ref> getAllBranchesList() {
        List<Ref> branches = new ArrayList<>();
        try {
            LOGGER.info("Listing branches.");
            branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        } catch (GitAPIException ex) {
            LOGGER.error(FETCH_ALL_BRANCHES_LIST_ERROR_MESSAGE, ex);
        }
        return branches;
    }

    /**
     * Get the name of the current active branch.
     */
    public String getCurrentBranchName() {
        String currentBranch= null;
        try {
            currentBranch = git.getRepository().getBranch();
        } catch(IOException ioe) {
            LOGGER.error(GETTING_CURRENT_BRANCH_NAME_ERROR_MESSAGE, ioe);
        }
        return currentBranch;
    }

    /** Try to switch to the given Git Branch.
     * First check if the given Branch is checked out. If no, clean the working repository then switch.
     * If the given Branch doesn't exist, try to create it or not according to the boolean createBranchIfNotExist.
     * @param testBranchNameParam The name of the Git Branch to create.
     * @param createBranchIfNotExist Either the branch must be created or not if it is not present.
     */
    public void manageBranchSwitching(String testBranchNameParam, boolean createBranchIfNotExist) throws GitBranchDoesNotExistException {
        LOGGER.info("Managing branch switch.");

        // 1 - Are we on the right Git Branch ?
        String currentBranch = getCurrentBranchName();
        LOGGER.info("Current Git Branch is '" + currentBranch + "'.");
        if (currentBranch != null && currentBranch.equals(testBranchNameParam)) {
            // 1.1 - Yep -> Job already done.
            LOGGER.info("The current Branch IS the right one.");
            return;
        }
        //  1.2 - Nope -> Clean the current local working repository [to be able to switch branch]
        LOGGER.info("The current Branch is NOT the right one.");
        cleanLocalWorkingRepository();

        // 2 - Branch exists locally or remotely ?
        List<Ref> branches = getAllBranchesList();
        if(!isBranchExist(branches, testBranchNameParam)) {
            // 2.1.1 - Nope
            if(createBranchIfNotExist) {
                /* If it doesn't exist remotely, we have to create it [only if asked]. */
                LOGGER.info("Creating Branch as asked.");
                createBranch(testBranchNameParam);
            } else {
                // If the Branch was supposed to exist, it is an Error.
                throw new GitBranchDoesNotExistException(testBranchNameParam);
            }
        }
        // 2.1.2 - Yep -> Switch to the Branch !
        switchBranch(testBranchNameParam);
    }

    /** This method tells if the given Git Branch exists among the given List.
     * @param branchListParam The {@link List} of Git Branches given as a List of {@link Ref}.
     * @param testBranchNameParam The name of the Git Branch we want to find.
     * @return True if the Branch is among the given List. False otherwise. */
    private boolean isBranchExist(List<Ref> branchListParam, String testBranchNameParam) {
        for(Ref ref : branchListParam) {
            String[] array = ref.getName().split("/");
            String branchName = array[array.length-1];
            if(testBranchNameParam.equals(branchName)) {
                LOGGER.info("Branch '" + testBranchNameParam + "' does exist.");
                return true;
            }
        }
        LOGGER.info("Branch '" + testBranchNameParam + "' does NOT exist.");
        return false;
    }


}