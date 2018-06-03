package tools;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.MemoryToolsRepository;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolsRepositoryException;

public class MemoryRepositoryTest {

    private static final MemoryToolsRepository REPOSITORY = new MemoryToolsRepository();



    @BeforeClass
    public static void init() throws ToolsRepositoryException {
        IToolDescriptor tool = new ToolDescriptor();
        tool.setName("Trimmomatic");
        REPOSITORY.insert(tool);

        tool = new ToolDescriptor();
        tool.setName("Velvet");
        REPOSITORY.insert(tool);

        tool = new ToolDescriptor();
        tool.setName("Blast");
        REPOSITORY.insert(tool);
    }



    @Test
    public void insertNonExistentToolTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.insertNonExistentToolTest(REPOSITORY);
    }

    @Test(expected = ToolsRepositoryException.class)
    public void insertExistentToolTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.insertExistentToolTest(REPOSITORY,"Blast");
    }


    @Test
    public void deleteNonExistentToolTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.deleteNonExistentToolTest(REPOSITORY);
    }

    @Test
    public void deleteExistentToolTest() throws ToolsRepositoryException {
        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(REPOSITORY);
            ToolsRepositoryTestUtils.deleteExistentToolTest(REPOSITORY, toolName);
        } finally {
            if(toolName != null)
                REPOSITORY.delete(toolName);
        }
    }


    @Test(expected = ToolsRepositoryException.class)
    public void updateNonExistentToolTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.updateNonExistentToolTest(REPOSITORY);
    }

    @Test
    public void updateExistentToolTest() throws ToolsRepositoryException {
        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(REPOSITORY);
            ToolsRepositoryTestUtils.updateExistentToolTest(REPOSITORY, toolName);
        } finally {
            if(toolName != null)
                REPOSITORY.delete(toolName);
        }
    }


    @Test
    public void getNonExistentToolTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.getNonExistentToolTest(REPOSITORY);
    }

    @Test
    public void getExistentToolTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.getExistentToolTest(REPOSITORY, "Blast");
    }


    @Test
    public void getAllWithEmptyResultTest() throws ToolsRepositoryException {
        MemoryToolsRepository repository = new MemoryToolsRepository();
        ToolsRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.getAllWithNonEmptyResultTest(REPOSITORY, "Blast", "Velvet", "Trimmomatic");
    }

}
