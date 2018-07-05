package pipelines;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.CachePipelinesRepository;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CacheRepositoryTest {

    private static class DummyPipelinesRepository implements IPipelinesRepository {

        private Map<String, IPipelineDescriptor> pipelinesByName;



        public DummyPipelinesRepository(boolean empty) {
            pipelinesByName = new HashMap<>();

            if(!empty) {
                IPipelineDescriptor pipeline = new PipelineDescriptor();
                pipeline.setName("Pipeline 1");
                pipelinesByName.put(pipeline.getName(), pipeline);

                pipeline = new PipelineDescriptor();
                pipeline.setName("Pipeline 2");
                pipelinesByName.put(pipeline.getName(), pipeline);
            }
        }



        @Override
        public String getLocation() throws PipelinesRepositoryException {
            return null;
        }

        @Override
        public Map<String, Object> getConfig() throws PipelinesRepositoryException {
            return null;
        }

        @Override
        public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }
            return pipelinesByName.values();
        }

        @Override
        public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }
            return pipelinesByName.get(pipelineName);
        }

        @Override
        public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }

            if(pipelinesByName.containsKey(pipeline.getName()))
                throw new PipelinesRepositoryException("There is already a pipeline with name:" + pipeline.getName());

            pipelinesByName.put(pipeline.getName(), pipeline);
        }

        @Override
        public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }

            if(!pipelinesByName.containsKey(pipeline.getName()))
                throw new PipelinesRepositoryException("There is not pipeline with name:" + pipeline.getName());

            pipelinesByName.put(pipeline.getName(), pipeline);
        }

        @Override
        public void delete(String pipelineName) throws PipelinesRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }

            pipelinesByName.remove(pipelineName);
        }
    }


    private static CachePipelinesRepository loadedRepository;



    @BeforeClass
    public static void init() throws PipelinesRepositoryException {
        loadedRepository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        loadedRepository.getAll();
    }


    @Test
    public void insertNonExistentPipelineTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.insertNonExistentPipelineTest(repository);
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void insertExistentPipelineTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.insertExistentPipelineTest(repository,"Pipeline 1");
    }


    @Test
    public void deleteNonExistentPipelineTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.deleteNonExistentPipelineTest(repository);
    }

    @Test
    public void deleteExistentPipelineTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));

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
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.updateNonExistentPipelineTest(repository);
    }

    @Test
    public void updateExistentPipelineTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));

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
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.getNonExistentPipelineTest(repository);
    }

    @Test
    public void getExistentPipelineTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.getExistentPipelineTest(repository, "Pipeline 1");
    }


    @Test
    public void getAllWithEmptyResultTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(true));
        PipelinesRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws PipelinesRepositoryException {
        CachePipelinesRepository repository = new CachePipelinesRepository(new DummyPipelinesRepository(false));
        PipelinesRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "Pipeline 1", "Pipeline 2");
    }


    @Test(timeout = 500)
    public void getLoadedTool() throws PipelinesRepositoryException {
        PipelinesRepositoryTestUtils.getExistentPipelineTest(loadedRepository, "Pipeline 1");
    }

}
