package pipelines;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pipelines.servers.EmptyPipelinesRepositoryServer;
import pipelines.servers.NotEmptyPipelinesRepositoryServer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.ServerPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.HashMap;

public class ServerRepositoryTest {

    private static String location;
    private static String emptyLocation;
    private static ConfigurableApplicationContext notEmptyServerContext;
    private static ConfigurableApplicationContext emptyServerContext;



    @BeforeClass
    public static void init() {
        notEmptyServerContext = SpringApplication.run(NotEmptyPipelinesRepositoryServer.class, "--server.port=4321");
        emptyServerContext = SpringApplication.run(EmptyPipelinesRepositoryServer.class, "--server.port=4322");

        location = "http://localhost:4321/notempty";
        emptyLocation = "http://localhost:4322/empty";
    }

    @AfterClass
    public static void end() {
        SpringApplication.exit(notEmptyServerContext);
        SpringApplication.exit(emptyServerContext);
    }



    @Test
    public void insertNonExistentPipelineTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.insertNonExistentPipelineTest(repository);
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void insertExistentPipelineTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.insertExistentPipelineTest(repository,"PipelineA");
    }


    @Test
    public void deleteNonExistentPipelineTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.deleteNonExistentPipelineTest(repository);
    }

    @Test
    public void deleteExistentPipelineTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());

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
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.updateNonExistentPipelineTest(repository);
    }

    @Test
    public void updateExistentPipelineTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());

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
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.getNonExistentPipelineTest(repository);
    }

    @Test
    public void getExistentPipelineTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.getExistentPipelineTest(repository, "PipelineA");
    }


    @Test
    public void getAllWithEmptyResultTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(emptyLocation, new HashMap<>());
        PipelinesRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws PipelinesRepositoryException {
        ServerPipelinesRepository repository = new ServerPipelinesRepository(location, new HashMap<>());
        PipelinesRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "PipelineA", "PipelineB");
    }

}
