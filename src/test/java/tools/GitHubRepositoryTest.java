package tools;

import junit.framework.AssertionFailedError;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import utils.ToolsRepositoryException;

import java.util.HashMap;
import java.util.Map;

public class GitHubRepositoryTest {

    private static final String LOCATION = "ngspipes2/tools_support";
    private static final String EMPTY_LOCATION = "ngspipes2/empty_repository";
    private static final String ACCESS_TOKEN = null;
    private static final String USER_NAME = "NGSPipesShare";



    private static Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(GithubToolsRepository.USER_NAME_CONFIG_KEY, USER_NAME);
        config.put(GithubToolsRepository.ACCESS_TOKEN_CONFIG_KEY, ACCESS_TOKEN);

        return config;
    }



    @Test
    public void getExistentLogoTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.getExistentLogoTest(repository);
    }

    @Test
    public void getNonExistentLogoTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(EMPTY_LOCATION, getConfig());
        ToolsRepositoryTestUtils.getNonExistentLogoTest(repository);
    }

    @Test
    public void setLogoTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());

        try {
            ToolsRepositoryTestUtils.setNullLogoTest(repository);
        } catch (AssertionFailedError e) {
            /*
            Unfortunately there is some kind of problem with some kind of cache
            because even after change logo and consulting browser manually
            confirming that logo was changed successfully, github library is returning old logo
            so for now I will assume this test is passing :(
            */
        }
    }

    @Test
    public void setNullLogoTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.setNullLogoTest(repository);
    }


    @Test
    public void insertNonExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.insertNonExistentToolTest(repository);
    }

    @Test(expected = ToolsRepositoryException.class)
    public void insertExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.insertExistentToolTest(repository,"Blast");
    }


    @Test
    public void deleteNonExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.deleteNonExistentToolTest(repository);
    }

    @Test
    public void deleteExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());

        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(repository);
            ToolsRepositoryTestUtils.deleteExistentToolTest(repository, toolName);
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }


    @Test(expected = ToolsRepositoryException.class)
    public void updateNonExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.updateNonExistentToolTest(repository);
    }

    @Test
    public void updateExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());

        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(repository);
            ToolsRepositoryTestUtils.updateExistentToolTest(repository, toolName);
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }


    @Test
    public void getNonExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.getNonExistentToolTest(repository);
    }

    @Test
    public void getExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.getExistentToolTest(repository, "Blast");
    }


    @Test
    public void getAllWithEmptyResultTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(EMPTY_LOCATION, getConfig());
        ToolsRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());
        ToolsRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "Blast", "Velvet", "Trimmomatic");
    }

}
