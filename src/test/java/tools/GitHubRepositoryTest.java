package tools;

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
