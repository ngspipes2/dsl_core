package pipelines;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.GithubPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.HashMap;
import java.util.Map;

public class GithubRepositoryTest {

    private static final String LOCATION = "ngspipes2/pipelines_support";
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
    public void getNonExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getNonExistentPipelineTest(repository);
    }

    @Test
    public void getExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getExistentPipelineTest(repository, "FirstStudyCase");
    }


    @Test
    public void getAllWithEmptyResultTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(EMPTY_LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "FirstStudyCase");
    }

}
