package tools;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pt.isel.ngspipes.dsl_core.descriptors.tool.ToolsRepositoryFactory;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.LocalToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.MemoryToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.ServerToolsRepository;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import tools.servers.NotEmptyToolsRepositoryServer;
import utils.ToolsRepositoryException;

import static org.junit.Assert.*;

public class ToolsRepositoryFactoryTest {

    @Test
    public void getExistentGithubRepositoryTest() throws ToolsRepositoryException {
        String location = "https://github.com/ngspipes2/tools_support";

        IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

        assertNotNull(repository);
        assertTrue(GithubToolsRepository.class.isAssignableFrom(repository.getClass()));
        assertEquals(3, repository.getAll().size());
    }

    @Test(expected = ToolsRepositoryException.class)
    public void getNonExistentGithubRepositoryTest() throws ToolsRepositoryException {
        String location = "https://github.com/ngspipes2/non_existest_repository";

        IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test
    public void getExistentLocalRepositoryTest() throws ToolsRepositoryException {
        String location = ClassLoader.getSystemResource("tools_repo").getPath();

        IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

        assertNotNull(repository);
        assertTrue(LocalToolsRepository.class.isAssignableFrom(repository.getClass()));
        assertEquals(3, repository.getAll().size());
    }

    @Test(expected = ToolsRepositoryException.class)
    public void getNonExistentLocalRepositoryTest() throws ToolsRepositoryException {
        String location = "D:/non_existent_repository";

        IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test
    public void getExistentServerRepositoryTest() throws ToolsRepositoryException {
        String location = "http://localhost:4321/notempty";
        ConfigurableApplicationContext server = null;

        try {
            server = SpringApplication.run(NotEmptyToolsRepositoryServer.class, "--server.port=4321");

            IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

            assertNotNull(repository);
            assertTrue(ServerToolsRepository.class.isAssignableFrom(repository.getClass()));
            assertEquals(3, repository.getAll().size());
        } finally {
            if(server != null)
                SpringApplication.exit(server);
        }
    }

    @Test(expected = ToolsRepositoryException.class)
    public void getNonExistentServerRepositoryTest() throws ToolsRepositoryException {
        String location = "http://localhost:4321/notempty";

        IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test(expected = ToolsRepositoryException.class)
    public void getRepositoryWithoutFactoryTest() throws ToolsRepositoryException {
        String location = "location";

        IToolsRepository repository = ToolsRepositoryFactory.create(location, null);

        assertNull(repository);
    }

    @Test
    public void getRepositoryWithRegisteredFactoryTest() throws ToolsRepositoryException {
        IToolsRepository repository = new MemoryToolsRepository();

        ToolsRepositoryFactory.registerFactory((location, config) -> {
            if(location.startsWith("memory://"))
                return repository;

            return null;
        });

        String location = "memory://repository";
        IToolsRepository obtainedRepository = ToolsRepositoryFactory.create(location, null);

        assertSame(repository, obtainedRepository);

        location = ClassLoader.getSystemResource("tools_repo").getPath();
        obtainedRepository = ToolsRepositoryFactory.create(location, null);

        assertNotSame(repository, obtainedRepository);
    }

}
