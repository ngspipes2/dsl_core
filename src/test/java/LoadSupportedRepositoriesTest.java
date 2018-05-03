import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.support.SupportRepository;
import utils.ToolRepositoryException;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class LoadSupportedRepositoriesTest {


    @Test
    public void loadSupportedRepositoriesTest() {
        //Arrange
        int expected = 4;

        //Act
        try {
            Collection<SupportRepository> supportedRepositories = new LinkedList<>();
            supportedRepositories.add(IOUtils.getRepositoriesSupportedData("Github"));
            supportedRepositories.add(IOUtils.getRepositoriesSupportedData("UriBased"));
            supportedRepositories.add(IOUtils.getRepositoriesSupportedData("LocalLinux"));
            supportedRepositories.add(IOUtils.getRepositoriesSupportedData("LocalWindows"));

            //Assert
            for (SupportRepository supportedRepo : supportedRepositories) {
                assertNotNull(supportedRepo);
            }
            assertEquals(expected, supportedRepositories.size());

        } catch (ToolRepositoryException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void loadNoExistentSupportedRepositoryTest() {
        //Arrange

        //Act
        try {
            SupportRepository supportedRepo = IOUtils.getRepositoriesSupportedData("Github1");

            //Assert
            assertNull(supportedRepo);

        } catch (ToolRepositoryException e) {
            fail("Should not have thrown any exception");
        }
    }
}
