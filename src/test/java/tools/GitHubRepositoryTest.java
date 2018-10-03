package tools;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import utils.ToolsRepositoryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        /*Can't call ToolsRepositoryTestUtils due to a cache problem when getting file from github*/
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, getConfig());

        byte[] logo = new byte[3];
        new Random().nextBytes(new byte[3]);

        repository.setLogo(logo);

        byte[] receivedLogo = repository.getLogo();

        assertNotNull(receivedLogo);
        assertEquals(logo.length, receivedLogo.length);

        for(int i=0; i<logo.length; ++i)
            assertEquals(logo[i], receivedLogo[i]);
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
