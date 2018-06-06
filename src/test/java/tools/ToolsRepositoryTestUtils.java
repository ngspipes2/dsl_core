package tools;

import pt.isel.ngspipes.tool_descriptor.implementations.CommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ToolsRepositoryTestUtils {

    public static String insertDummyTool(IToolsRepository repository) throws ToolsRepositoryException {
        String toolName = UUID.randomUUID().toString();

        IToolDescriptor tool = new ToolDescriptor();
        tool.setName(toolName);

        tool.setCommands(new LinkedList<>());
        tool.getCommands().add(new CommandDescriptor());
        tool.getCommands().stream().findFirst().get().setName("Dummy Command");

        tool.setExecutionContexts(new LinkedList<>());
        tool.getExecutionContexts().add(new ExecutionContextDescriptor());
        tool.getExecutionContexts().stream().findFirst().get().setName("Dummy Execution Context");

        repository.insert(tool);

        return toolName;
    }


    public static void insertNonExistentToolTest(IToolsRepository repository) throws ToolsRepositoryException {
        String toolName = null;

        try {
            toolName = insertDummyTool(repository);

            IToolDescriptor tool = repository.get(toolName);

            assertNotNull(tool);
            assertEquals(toolName, tool.getName());
            assertEquals(1, tool.getCommands().size());
            assertEquals("Dummy Command", tool.getCommands().stream().findFirst().get().getName());
            assertEquals(1, tool.getExecutionContexts().size());
            assertEquals("Dummy Execution Context", tool.getExecutionContexts().stream().findFirst().get().getName());
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }

    public static void insertExistentToolTest(IToolsRepository repository, String existentToolName) throws ToolsRepositoryException {
        IToolDescriptor tool = new ToolDescriptor();
        tool.setName(existentToolName);

        repository.insert(tool);
    }


    public static void deleteNonExistentToolTest(IToolsRepository repository) throws ToolsRepositoryException {
        String toolName = UUID.randomUUID().toString();

        repository.delete(toolName);
    }

    public static void deleteExistentToolTest(IToolsRepository repository, String toolName) throws ToolsRepositoryException {
        repository.delete(toolName);

        IToolDescriptor tool = repository.get(toolName);

        assertNull(tool);
    }


    public static void updateNonExistentToolTest(IToolsRepository repository) throws ToolsRepositoryException {
        String toolName = UUID.randomUUID().toString();
        String author = UUID.randomUUID().toString();

        IToolDescriptor tool = new ToolDescriptor();
        tool.setName(toolName);
        tool.setAuthor(author);

        repository.update(tool);
    }

    public static void updateExistentToolTest(IToolsRepository repository, String toolName) throws ToolsRepositoryException {
        String author = UUID.randomUUID().toString();

        IToolDescriptor tool = new ToolDescriptor();
        tool.setName(toolName);
        tool.setAuthor(author);
        tool.setCommands(new LinkedList<>());
        tool.setExecutionContexts(new LinkedList<>());

        repository.update(tool);

        tool = repository.get(toolName);

        assertNotNull(tool);
        assertEquals(toolName, tool.getName());
        assertEquals(author, tool.getAuthor());
        assertEquals(0, tool.getCommands().size());
        assertEquals(0, tool.getExecutionContexts().size());
    }


    public static void getNonExistentToolTest(IToolsRepository repository) throws ToolsRepositoryException {
        String toolName = UUID.randomUUID().toString();

        IToolDescriptor tool = repository.get(toolName);

        assertNull(tool);
    }

    public static void getExistentToolTest(IToolsRepository repository, String toolName) throws ToolsRepositoryException {
        IToolDescriptor tool = repository.get(toolName);

        assertNotNull(tool);
        assertEquals(toolName, tool.getName());
    }


    public static void getAllWithEmptyResultTest(IToolsRepository repository) throws ToolsRepositoryException {
        Collection<IToolDescriptor> tools = repository.getAll();

        assertNotNull(tools);
        assertEquals(0, tools.size());
    }

    public static void getAllWithNonEmptyResultTest(IToolsRepository repository, String... expectedToolsNames) throws ToolsRepositoryException {
        Collection<IToolDescriptor> tools = repository.getAll();

        assertNotNull(tools);
        assertEquals(expectedToolsNames.length, tools.size());

        Collection<String> toolsNames = tools.stream().map(IToolDescriptor::getName).collect(Collectors.toList());
        for(String toolName : expectedToolsNames)
            assertTrue(toolsNames.contains(toolName));
    }

}
