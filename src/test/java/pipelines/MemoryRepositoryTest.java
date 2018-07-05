package pipelines;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.MemoryPipelinesRepository;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

public class MemoryRepositoryTest {

    private static final MemoryPipelinesRepository REPOSITORY = new MemoryPipelinesRepository();



    @BeforeClass
    public static void init() throws PipelinesRepositoryException {
        IPipelineDescriptor pipeline = new PipelineDescriptor();
        pipeline.setName("Pipeline 1");
        REPOSITORY.insert(pipeline);

        pipeline = new PipelineDescriptor();
        pipeline.setName("Pipeline 2");
        REPOSITORY.insert(pipeline);
    }



    @Test
    public void insertNonExistentPipelineTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.insertNonExistentPipelineTest(REPOSITORY);
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void insertExistentPipelineTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.insertExistentPipelineTest(REPOSITORY,"Pipeline 1");
    }


    @Test
    public void deleteNonExistentPipelineTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.deleteNonExistentPipelineTest(REPOSITORY);
    }

    @Test
    public void deleteExistentPipelineTest() throws PipelinesRepositoryException {
        String pipelineName = null;

        try {
            pipelineName = PipelinesRepositoryTestUtils.insertDummyPipeline(REPOSITORY);
            PipelinesRepositoryTestUtils.deleteExistentPipelineTest(REPOSITORY, pipelineName);
        } finally {
            if(pipelineName != null)
                REPOSITORY.delete(pipelineName);
        }
    }


    @Test(expected = PipelinesRepositoryException.class)
    public void updateNonExistentPipelineTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.updateNonExistentPipelineTest(REPOSITORY);
    }

    @Test
    public void updateExistentPipelineTest() throws PipelinesRepositoryException {
        String pipelineName = null;

        try {
            pipelineName = PipelinesRepositoryTestUtils.insertDummyPipeline(REPOSITORY);
            PipelinesRepositoryTestUtils.updateExistentPipelineTest(REPOSITORY, pipelineName);
        } finally {
            if(pipelineName != null)
                REPOSITORY.delete(pipelineName);
        }
    }


    @Test
    public void getNonExistentPipelineTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.getNonExistentPipelineTest(REPOSITORY);
    }

    @Test
    public void getExistentPipelineTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.getExistentPipelineTest(REPOSITORY, "Pipeline 1");
    }


    @Test
    public void getAllWithEmptyResultTest() throws PipelinesRepositoryException {
        MemoryPipelinesRepository repository = new MemoryPipelinesRepository();
        PipelinesRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.getAllWithNonEmptyResultTest(REPOSITORY, "Pipeline 1", "Pipeline 2");
    }

}
