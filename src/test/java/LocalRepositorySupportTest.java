import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.LocalToolsRepository;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolsRepositoryException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LocalRepositorySupportTest {

    private static final String LOCATION;



    static {
        LOCATION = ClassLoader.getSystemResource("tools_repo").getPath();
    }



    @Test
    public void localRepositoryGetToolTest() {
        //Arrange
        String name = "Blast";

        // Act
        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(LOCATION, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);
            assertEquals(2, toolDescriptor.getExecutionContexts().size());
        } catch(ToolsRepositoryException e) {
            fail("Shouldn't throw exception");
        }

        //Assert
    }


    @Test
    public void localRepositoryInsertToolTest() {
        //Arrange
        String name = "Blast";
        String newName = "Blast1";

        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(LOCATION, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);
            toolDescriptor.setName(newName);

            int sizeBeforeInsert = localRepository.getAll().size();

            // Act
            localRepository.insert(toolDescriptor);

            //Assert
            int expected = sizeBeforeInsert + 1;
            int size = localRepository.getAll().size();
             assertEquals(expected, size);

            localRepository.delete(newName);
        } catch(ToolsRepositoryException e) {
            fail("Shouldn't throw exception");
        }
    }


    @Test(expected = ToolsRepositoryException.class)
    public void localRepositoryInsertExistentToolTest() throws ToolsRepositoryException {
        //Arrange
        String name = "Blast";

        // Act
        LocalToolsRepository localRepository = new LocalToolsRepository(LOCATION, null);
        IToolDescriptor toolDescriptor = localRepository.get(name);
        localRepository.insert(toolDescriptor);

        //Assert
    }


    @Test
    public void localRepositoryUpdateToolTest() {
        //Arrange
        String name = "Blast";
        String newName = "Blast1";

        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(LOCATION, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);
            toolDescriptor.setName(newName);
            localRepository.insert(toolDescriptor);

            IToolDescriptor toUpdate = localRepository.get(newName);
            String newAuthor = "Calmen";
            toUpdate.setAuthor(newAuthor);
            localRepository.update(toUpdate);

            // Act
            IToolDescriptor updated = localRepository.get(newName);

            //Assert
            assertEquals(newAuthor, updated.getAuthor());

            localRepository.delete(newName);
        } catch(ToolsRepositoryException e) {
            fail("Shouldn't throw exception");
        }
    }

    @Test(expected = ToolsRepositoryException.class)
    public void localRepositoryUpdateNotExistentToolTest() throws ToolsRepositoryException {
        //Arrange
        String name = "BlastN";

        // Act
        LocalToolsRepository localRepository = new LocalToolsRepository(LOCATION, null);
        ToolDescriptor toUpdate = new ToolDescriptor();
        toUpdate.setName(name);
        localRepository.update(toUpdate);

        //Assert

    }

    @Test
    public void localRepositoryDeleteToolTest() {
        //Arrange
        String name = "Blast";
        String newName = "Blast1";


        try {
            LocalToolsRepository localRepository = new LocalToolsRepository(LOCATION, null);
            IToolDescriptor toolDescriptor = localRepository.get(name);
            toolDescriptor.setName(newName);
            int sizeBeforeInsert = localRepository.getAll().size();

            localRepository.insert(toolDescriptor);

            // Act
            localRepository.delete(newName);


            //Assert
            int size = localRepository.getAll().size();
            assertEquals(sizeBeforeInsert, size);
        } catch(ToolsRepositoryException e) {
            fail("Shouldn't throw exception");
        }
    }
}
