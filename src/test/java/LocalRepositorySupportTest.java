import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities.JacksonToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.LocalToolsRepository;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolRepositoryException;

import static org.junit.Assert.*;

public class LocalRepositorySupportTest {


    @Test
    public void localRepositorySupportTest() {
        //Arrange
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
        } catch(ToolRepositoryException e) {
            fail("Slouhdn't throw exception");
        }

        //Assert
    }

    @Test
    public void localRepositoryGetToolTest() {
        //Arrange
        String name = "Blast";
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);
            assertEquals(2, toolDescriptor.getExecutionContexts().size());
        } catch(ToolRepositoryException e) {
            fail("Slouhdn't throw exception");
        }

        //Assert
    }


    @Test
    public void localRepositoryInsertToolTest() {
        //Arrange
        String name = "Blast";
        String newName = "Blast1";
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);

            int sizeBeforeInsert = localRepository.getAll().size();

            ToolDescriptor newTool = new JacksonToolDescriptor();
            newTool.setAuthor(toolDescriptor.getAuthor());
            newTool.setName(newName);
            newTool.setCommands(toolDescriptor.getCommands());
            newTool.setDescription(toolDescriptor.getDescription());
            newTool.setLogo(toolDescriptor.getLogo());
            newTool.setVersion(toolDescriptor.getVersion());
            newTool.setDocumentation(toolDescriptor.getDocumentation());
            newTool.setExecutionContexts(toolDescriptor.getExecutionContexts());

            localRepository.insert(newTool);

            //Assert
            int expected = sizeBeforeInsert + 1;
            int size = localRepository.getAll().size();
            assertEquals(expected, size);

            localRepository.delete(newName);
        } catch(ToolRepositoryException e) {
            fail("Slouhdn't throw exception");
        }
    }


    @Test(expected = ToolRepositoryException.class)
    public void localRepositoryInsertExistentToolTest() {
        //Arrange
        String name = "Blast";
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
        IToolDescriptor toolDescriptor = localRepository.get(name);
        localRepository.insert(toolDescriptor);

        //Assert
    }


    @Test
    public void localRepositoryUpdateToolTest() {
        //Arrange
        String name = "Blast";
        String newName = "Blast1";
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);

            int sizeBeforeInsert = localRepository.getAll().size();

            ToolDescriptor newTool = new JacksonToolDescriptor();
            newTool.setAuthor(toolDescriptor.getAuthor());
            newTool.setName(newName);
            newTool.setCommands(toolDescriptor.getCommands());
            newTool.setDescription(toolDescriptor.getDescription());
            newTool.setLogo(toolDescriptor.getLogo());
            newTool.setVersion(toolDescriptor.getVersion());
            newTool.setDocumentation(toolDescriptor.getDocumentation());
            newTool.setExecutionContexts(toolDescriptor.getExecutionContexts());

            localRepository.insert(newTool);

            ToolDescriptor toUpdate = (ToolDescriptor) localRepository.get(newName);
            String newAuthor = "Calmen";
            toUpdate.setAuthor(newAuthor);
            localRepository.update(toUpdate);

            IToolDescriptor updated = localRepository.get(newName);

            //Assert
            assertEquals(newAuthor, updated.getAuthor());

            localRepository.delete(newName);
        } catch(ToolRepositoryException e) {
            fail("Slouhdn't throw exception");
        }
    }

    @Test(expected = ToolRepositoryException.class)
    public void localRepositoryUpdateNotExistentToolTest() {
        //Arrange
        String name = "BlastN";
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
        ToolDescriptor toUpdate = new JacksonToolDescriptor();
        toUpdate.setName(name);
        localRepository.update(toUpdate);

        //Assert

    }

    @Test
    public void localRepositoryDeleteToolTest() {
        //Arrange
        String name = "Blast";
        String newName = "Blast1";
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support_local";

        // Act
        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(location, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);

            int sizeBeforeInsert = localRepository.getAll().size();

            ToolDescriptor newTool = new JacksonToolDescriptor();
            newTool.setAuthor(toolDescriptor.getAuthor());
            newTool.setName(newName);
            newTool.setCommands(toolDescriptor.getCommands());
            newTool.setDescription(toolDescriptor.getDescription());
            newTool.setLogo(toolDescriptor.getLogo());
            newTool.setVersion(toolDescriptor.getVersion());
            newTool.setDocumentation(toolDescriptor.getDocumentation());
            newTool.setExecutionContexts(toolDescriptor.getExecutionContexts());

            localRepository.insert(newTool);
            localRepository.delete(newName);


            //Assert
            int size = localRepository.getAll().size();
            assertEquals(sizeBeforeInsert, size);
        } catch(ToolRepositoryException e) {
            fail("Slouhdn't throw exception");
        }
    }
}
