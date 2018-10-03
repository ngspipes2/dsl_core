package tools;

import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.LocalToolsRepository;
import utils.ToolsRepositoryException;

public class LocalRepositoryTest {

    private static final String LOCATION;
    private static final String EMPTY_LOCATION;



    static {
        LOCATION = ClassLoader.getSystemResource("tools_repo").getPath();
        EMPTY_LOCATION = ClassLoader.getSystemResource("empty_tools_repo").getPath();
    }



    @Test
    public void getExistentLogoTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getExistentLogoTest(repository);
    }

    @Test
    public void getNonExistentLogoTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(EMPTY_LOCATION, null);
        ToolsRepositoryTestUtils.getNonExistentLogoTest(repository);
    }

    @Test
    public void setLogoTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.setLogoTest(repository);
    }

    @Test
    public void setNullLogoTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.setNullLogoTest(repository);
    }


    @Test
    public void insertNonExistentToolTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.insertNonExistentToolTest(repository);
    }

    @Test(expected = ToolsRepositoryException.class)
    public void insertExistentToolTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.insertExistentToolTest(repository,"Blast");
    }


    @Test
    public void deleteNonExistentToolTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.deleteNonExistentToolTest(repository);
    }

    @Test
    public void deleteExistentToolTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);

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
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.updateNonExistentToolTest(repository);
    }

    @Test
    public void updateExistentToolTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);

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
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getNonExistentToolTest(repository);
    }

    @Test
    public void getExistentToolTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getExistentToolTest(repository, "Blast");
    }


    @Test
    public void getAllWithEmptyResultTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(EMPTY_LOCATION, null);
        ToolsRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws ToolsRepositoryException {
        LocalToolsRepository repository = new LocalToolsRepository(LOCATION, null);
        ToolsRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "Blast", "Velvet", "Trimmomatic");
    }

}
