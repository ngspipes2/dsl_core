package tools;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import utils.ToolsRepositoryException;

public class GitHubRepositorySupportTest {

    private static final String LOCATION = "https://github.com/ngspipes2/tools_support";
    private static final String EMPTY_LOCATION = "https://github.com/ngspipes2/empty_repository";



    @Test
    public void getNonExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getNonExistentToolTest(repository);
    }

    @Test
    public void getExistentToolTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getExistentToolTest(repository, "Blast");
    }


    @Test
    public void getAllWithEmptyResultTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(EMPTY_LOCATION, null);
        ToolsRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws ToolsRepositoryException {
        GithubToolsRepository repository = new GithubToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "Blast", "Velvet", "Trimmomatic");
    }

}
