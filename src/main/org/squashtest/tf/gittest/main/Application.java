package org.squashtest.tf.gittest.main;

import org.squashtest.tf.gittest.controller.SourceCodeManagementController;
import org.squashtest.tf.gittest.domain.GitProjectSettings;
import org.squashtest.tf.gittest.domain.TestCaseDto;

/**
 * Created by jlor on 01/08/2018.
 */
public class Application {
    /* Attributes for the Git Settings of a SquashTM Project. */
    private static final String LOCAL_REPOSITORY = "C:/Users/jlor/workspace/fake-repo-two//";
    private static final String FEATURE_FOLDER_RELATIVE_PATH = "features"; // "src/test/resources/stories";
    private static final String REMOTE_REPOSITORY = "https://github.com/Grelotine/fake-repo-two.git";
    private static final String MAIN_BRANCH_NAME = "master";
    private static final String ANOTHER_BRANCH_NAME = "fake_branch";
    private static final String TEST_BRANCH_NAME = "test_branch";

    private static final String GIT_USERNAME = "AnotherGitUser4";
    private static final String GIT_PASS = "8A8b8C8d8E";

    /* Attributes for the Test Case to be published to Git. */
    private static final Long TEST_CASE_ID = 888L;
    private static final String TEST_CASE_NAME = "extinction";
    private static final String GHERKIN_SCRIPT_EXTINCTION =
            "Feature: Eteindre le générateur du vaisseau.\n" +
            "En tant que pilote, ou membre d'équipage, je souhaite éteindre le générateur du vaisseau afin de l'arrêter.\n" +
            "\n" +
            "Scenario: Extinction\n" +
            "Given je suis bord du vaisseau\n" +
            "When j'appuie sur le bouton d'extinction\n" +
            "Then le générateur s'éteint";

    public static void main(String[] args) {
        SourceCodeManagementController controller = new SourceCodeManagementController();

        /* Initializing Objects. Boolean createBranchIfExist is set to false, meaning the branch should already exist. */
        TestCaseDto oneTestCase =  new TestCaseDto(TEST_CASE_ID, TEST_CASE_NAME, GHERKIN_SCRIPT_EXTINCTION);
        GitProjectSettings gitProjectSettings = new GitProjectSettings(
                LOCAL_REPOSITORY, REMOTE_REPOSITORY,
                ANOTHER_BRANCH_NAME, false,
                FEATURE_FOLDER_RELATIVE_PATH,
                GIT_USERNAME, GIT_PASS);

        /* Publishing the single TestCase. */
        controller.publishTestCaseToGit(
                gitProjectSettings.getLocalRepositoryPath(), gitProjectSettings.getRemoteRepositoryPath(),
                gitProjectSettings.getTargetBranchName(), gitProjectSettings.isCreateBranchIfNotExist(),
                gitProjectSettings.getFeatureFolderRelativePath(),
                oneTestCase.getName(), oneTestCase.getGherkinScript(),
                gitProjectSettings.getGitUsername(), gitProjectSettings.getGitPassword());
    }
}
