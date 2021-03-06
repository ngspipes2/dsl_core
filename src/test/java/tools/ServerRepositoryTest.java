package tools;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.ServerToolsRepository;
import tools.servers.EmptyToolsRepositoryServer;
import tools.servers.NotEmptyToolsRepositoryServer;
import utils.ToolsRepositoryException;

import java.util.HashMap;

public class ServerRepositoryTest {

    private static String location;
    private static String emptyLocation;
    private static ConfigurableApplicationContext notEmptyServerContext;
    private static ConfigurableApplicationContext emptyServerContext;



    @BeforeClass
    public static void init() {
        notEmptyServerContext = SpringApplication.run(NotEmptyToolsRepositoryServer.class, "--server.port=4321");
        emptyServerContext = SpringApplication.run(EmptyToolsRepositoryServer.class, "--server.port=4322");

        location = "http://localhost:4321/notempty";
        emptyLocation = "http://localhost:4322/empty";
    }

    @AfterClass
    public static void end() {
        SpringApplication.exit(notEmptyServerContext);
        SpringApplication.exit(emptyServerContext);
    }


    @Test
    public void getExistentLogoTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.getExistentLogoTest(repository);
    }

    @Test
    public void getNonExistentLogoTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(emptyLocation, new HashMap<>());
        ToolsRepositoryTestUtils.getNonExistentLogoTest(repository);
    }

    @Test
    public void setLogoTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.setLogoTest(repository);
    }

    @Test
    public void setNullLogoTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.setNullLogoTest(repository);
    }


    @Test
    public void getToolsNamesWithEmptyResultTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(emptyLocation, new HashMap<>());
        ToolsRepositoryTestUtils.getToolsNamesWithEmptyResultTest(repository);
    }

    @Test
    public void getToolsNamesWithNonEmptyResult() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.getToolsNamesWithNonEmptyResultTest(repository, "Blast", "Velvet", "Trimmomatic");
    }


    @Test
    public void insertNonExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.insertNonExistentToolTest(repository);
    }

    @Test(expected = ToolsRepositoryException.class)
    public void insertExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.insertExistentToolTest(repository,"Blast");
    }


    @Test
    public void deleteNonExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.deleteNonExistentToolTest(repository);
    }

    @Test
    public void deleteExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());

        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(repository);
            ToolsRepositoryTestUtils.deleteExistentToolTest(repository, toolName);
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }


    @Test(expected = ToolsRepositoryException.class)
    public void updateNonExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.updateNonExistentToolTest(repository);
    }

    @Test
    public void updateExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());

        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(repository);
            ToolsRepositoryTestUtils.updateExistentToolTest(repository, toolName);
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }


    @Test
    public void getNonExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.getNonExistentToolTest(repository);
    }

    @Test
    public void getExistentToolTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.getExistentToolTest(repository, "Blast");
    }


    @Test
    public void getAllWithEmptyResultTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(emptyLocation, new HashMap<>());
        ToolsRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws ToolsRepositoryException {
        ServerToolsRepository repository = new ServerToolsRepository(location, new HashMap<>());
        ToolsRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "Blast", "Velvet", "Trimmomatic");
    }

}
