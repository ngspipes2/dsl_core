package pipelines;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.GithubPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.HashMap;
import java.util.Map;

public class GithubRepositoryTest {

    private static final String LOCATION = "https://github.com/ngspipes2/pipelines_support";
    private static final String EMPTY_LOCATION = "https://github.com/ngspipes2/empty_repository";
    private static final String ACCESS_TOKEN = null;
    private static final String USER_NAME = "NGSPipesShare";



    private static Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(GithubToolsRepository.USER_NAME_CONFIG_KEY, USER_NAME);
        config.put(GithubToolsRepository.ACCESS_TOKEN_CONFIG_KEY, ACCESS_TOKEN);

        return config;
    }


    @Test
    public void getExistentLogoTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getExistentLogoTest(repository);
    }

    @Test
    public void getNonExistentLogoTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(EMPTY_LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getNonExistentLogoTest(repository);
    }

    @Test
    public void setLogoTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.setNullLogoTest(repository);
    }

    @Test
    public void setNullLogoTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.setNullLogoTest(repository);
    }


    @Test
    public void getPipelinesNamesWithEmptyResultTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(EMPTY_LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getPipelinesNamesWithEmptyResultTest(repository);
    }

    @Test
    public void getPipelinesNamesWithNonEmptyResult() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.getPipelinesNamesWithNonEmptyResultTest(repository, "FirstStudyCase");
    }


    @Test
    public void insertNonExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig(), PipelineSerialization.Format.YAML);
        PipelinesRepositoryTestUtils.insertNonExistentPipelineTest(repository);
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void insertExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.insertExistentPipelineTest(repository,"FirstStudyCase");
    }


    @Test
    public void deleteNonExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.deleteNonExistentPipelineTest(repository);
    }

    @Test
    public void deleteExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());

        String pipelineName = null;

        try {
            pipelineName = PipelinesRepositoryTestUtils.insertDummyPipeline(repository);
            PipelinesRepositoryTestUtils.deleteExistentPipelineTest(repository, pipelineName);
        } finally {
            if(pipelineName != null)
                repository.delete(pipelineName);
        }
    }


    @Test(expected = PipelinesRepositoryException.class)
    public void updateNonExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());
        PipelinesRepositoryTestUtils.updateNonExistentPipelineTest(repository);
    }

    @Test
    public void updateExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, getConfig());

        String pipelineName = null;

        try {
            pipelineName = PipelinesRepositoryTestUtils.insertDummyPipeline(repository);
            PipelinesRepositoryTestUtils.updateExistentPipelineTest(repository, pipelineName);
        } finally {
            if(pipelineName != null)
                repository.delete(pipelineName);
        }
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
