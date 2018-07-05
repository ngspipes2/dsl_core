package pipelines;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.GithubPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

public class GithubRepositoryTest {

    private static final String LOCATION = "https://github.com/ngspipes2/pipelines_support";
    private static final String EMPTY_LOCATION = "https://github.com/ngspipes2/empty_repository";



    @Test
    public void getNonExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getNonExistentPipelineTest(repository);
    }

    @Test
    public void getExistentPipelineTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getExistentPipelineTest(repository, "FirstStudyCase");
    }


    @Test
    public void getAllWithEmptyResultTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(EMPTY_LOCATION, null);
        PipelinesRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws PipelinesRepositoryException {
        GithubPipelinesRepository repository = new GithubPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "FirstStudyCase");
    }

}
