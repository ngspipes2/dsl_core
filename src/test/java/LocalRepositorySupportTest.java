import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.LocalToolsRepository;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolRepositoryException;

import static org.junit.Assert.*;

public class LocalRepositorySupportTest {


    @Test
    public void localRepositorySupportTest() {
        //Arrange
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support";

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
        String location = "E:\\Work\\NGSPipes\\ngspipes2\\tools_support";

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
}
