import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolsRepositoryException;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GitHubRepositorySupportTest {


    @Test
    public void githubRepositorySupportTest() {
        //Arrange
        String location = "https://github.com/ngspipes/tools";

        // Act
        GithubToolsRepository repo = new GithubToolsRepository(location, null);

        //Assert
    }

    @Test
    public void githubRepositoryGetToolTest() {
        //Arrange
        String location = "https://github.com/ngspipes2/tools_support";
        String name = "Trimmomatic";

        // Act
        try{
            GithubToolsRepository repo = new GithubToolsRepository(location, null);
            IToolDescriptor toolDescriptor = repo.get(name);
            assertEquals(2, toolDescriptor.getExecutionContexts().size());
        } catch (ToolsRepositoryException e) {
            fail("Shouldn't generate exception.");
        }

        //Assert
    }

    @Test
    public void githubRepositoryGetAllToolTest() {
        //Arrange
        String location = "https://github.com/ngspipes2/tools_support";

        // Act
        try{
            GithubToolsRepository repo = new GithubToolsRepository(location, null);
            Collection<IToolDescriptor> toolDescriptors = repo.getAll();
            assertEquals(3, toolDescriptors.size());
        } catch (ToolsRepositoryException e) {
            fail("Shouldn't generate exception.");
        }

        //Assert
    }
/*
    @Test(expected = DSLCoreException.class)
    public void githubRepositorySupportWithBadLocationTest() {
        //Arrange
        String location = "https://github1.com/ngspipes/tools";

        // Act
        GithubToolsRepository repo = new GithubToolsRepository(location, null);

        //Assert
    }*/
}
