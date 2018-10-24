package pipelines;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pipelines.servers.NotEmptyPipelinesRepositoryServer;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.PipelinesRepositoryFactory;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.GithubPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.LocalPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.MemoryPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.ServerPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PipelinesRepositoryFactoryTest {

    private static final String ACCESS_TOKEN = null;
    private static final String USER_NAME = "NGSPipesShare";



    private static Map<String, Object> getGithubConfig() {
        Map<String, Object> config = new HashMap<>();

        config.put(GithubToolsRepository.USER_NAME_CONFIG_KEY, USER_NAME);
        config.put(GithubToolsRepository.ACCESS_TOKEN_CONFIG_KEY, ACCESS_TOKEN);

        return config;
    }



    @Test
    public void getExistentGithubRepositoryTest() throws PipelinesRepositoryException {
        String location = "https://github.com/ngspipes2/pipelines_support";

        IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, getGithubConfig());

        assertNotNull(repository);
        assertTrue(GithubPipelinesRepository.class.isAssignableFrom(repository.getClass()));
        assertEquals(1, repository.getAll().size());
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void getNonExistentGithubRepositoryTest() throws PipelinesRepositoryException {
        String location = "https://github.com/ngspipes2/non_existest_repository";

        IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, getGithubConfig());

        assertNull(repository);
    }

    @Test
    public void getExistentLocalRepositoryTest() throws PipelinesRepositoryException {
        String location = ClassLoader.getSystemResource("pipelines_repo").getPath();

        IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, null);

        assertNotNull(repository);
        assertTrue(LocalPipelinesRepository.class.isAssignableFrom(repository.getClass()));
        assertEquals(1, repository.getAll().size());
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void getNonExistentLocalRepositoryTest() throws PipelinesRepositoryException {
        String location = "D:/non_existent_repository";

        IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test
    public void getExistentServerRepositoryTest() throws PipelinesRepositoryException {
        String location = "http://localhost:4321/notempty";
        ConfigurableApplicationContext server = null;

        try {
            server = SpringApplication.run(NotEmptyPipelinesRepositoryServer.class, "--server.port=4321");

            IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, null);

            assertNotNull(repository);
            assertTrue(ServerPipelinesRepository.class.isAssignableFrom(repository.getClass()));
            assertEquals(2, repository.getAll().size());
        } finally {
            if(server != null)
                SpringApplication.exit(server);
        }
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void getNonExistentServerRepositoryTest() throws PipelinesRepositoryException {
        String location = "http://localhost:4321/notempty";

        IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test(expected = PipelinesRepositoryException.class)
    public void getRepositoryWithoutFactoryTest() throws PipelinesRepositoryException {
        String location = "location";

        IPipelinesRepository repository = PipelinesRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test
    public void getRepositoryWithRegisteredFactoryTest() throws PipelinesRepositoryException {
        IPipelinesRepository repository = new MemoryPipelinesRepository();

        PipelinesRepositoryFactory.registerFactory((location, config) -> {
            if(location.startsWith("memory://"))
                return repository;

            return null;
        });

        String location = "memory://repository";
        IPipelinesRepository obtainedRepository = PipelinesRepositoryFactory.create(location, null);

        assertSame(repository, obtainedRepository);

        location = ClassLoader.getSystemResource("pipelines_repo").getPath();
        obtainedRepository = PipelinesRepositoryFactory.create(location, null);

        assertNotSame(repository, obtainedRepository);
    }

}
