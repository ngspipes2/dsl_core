import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolRepositoryException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class GitHubRepositorySupportTest {


    @Test
    public void githubRepositorySupportTest() {
        //Arrange
        String location = "https://github.com/ngspipes/tools";

        // Act
        try{
            GithubToolsRepository repo = new GithubToolsRepository(location, null);
        } catch (ToolRepositoryException e) {
            fail("Shouldn't generate exception.");
        }

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
        } catch (ToolRepositoryException e) {
            fail("Shouldn't generate exception.");
        }

        //Assert
    }

    @Test(expected = ToolRepositoryException.class)
    public void githubRepositorySupportWithBadLocationTest() {
        //Arrange
        String location = "https://github1.com/ngspipes/tools";

        // Act
        GithubToolsRepository repo = new GithubToolsRepository(location, null);

        //Assert
    }
}
