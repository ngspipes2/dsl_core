package pipelines;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.LocalPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

public class LocalRepositoryTest {

    private static final String LOCATION;
    private static final String EMPTY_LOCATION;



    static {
        LOCATION = ClassLoader.getSystemResource("pipelines_repo").getPath();
        EMPTY_LOCATION = ClassLoader.getSystemResource("empty_pipelines_repo").getPath();
    }


    
    @Test
    public void getExistentLogoTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getExistentLogoTest(repository);
    }

    @Test
    public void getNonExistentLogoTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(EMPTY_LOCATION, null);
        PipelinesRepositoryTestUtils.getNonExistentLogoTest(repository);
    }

    @Test
    public void setLogoTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.setLogoTest(repository);
    }

    @Test
    public void setNullLogoTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.setNullLogoTest(repository);
    }


    @Test
    public void insertNonExistentPipelineTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null, PipelineSerialization.Format.YAML);
        PipelinesRepositoryTestUtils.insertNonExistentPipelineTest(repository);
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void insertExistentPipelineTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.insertExistentPipelineTest(repository,"FirstStudyCase");
    }


    @Test
    public void deleteNonExistentPipelineTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.deleteNonExistentPipelineTest(repository);
    }

    @Test
    public void deleteExistentPipelineTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);

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
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.updateNonExistentPipelineTest(repository);
    }

    @Test
    public void updateExistentPipelineTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);

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
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getNonExistentPipelineTest(repository);
    }

    @Test
    public void getExistentPipelineTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getExistentPipelineTest(repository, "FirstStudyCase");
    }


    @Test
    public void getAllWithEmptyResultTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(EMPTY_LOCATION, null);
        PipelinesRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws PipelinesRepositoryException {
        LocalPipelinesRepository repository = new LocalPipelinesRepository(LOCATION, null);
        PipelinesRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "FirstStudyCase");
    }

}
