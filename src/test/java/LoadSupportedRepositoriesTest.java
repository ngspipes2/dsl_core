import implementations.SupportedRepository;
import org.junit.Test;
import utils.RepositoryException;
import utils.Util;

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
            Collection<SupportedRepository> supportedRepositories = new LinkedList<>();
            supportedRepositories.add(Util.getRepositoriesSupportedData("Github"));
            supportedRepositories.add(Util.getRepositoriesSupportedData("UriBased"));
            supportedRepositories.add(Util.getRepositoriesSupportedData("LocalLinux"));
            supportedRepositories.add(Util.getRepositoriesSupportedData("LocalWindows"));

            //Assert
            for (SupportedRepository supportedRepo : supportedRepositories) {
                assertNotNull(supportedRepo);
            }
            assertEquals(expected, supportedRepositories.size());

        } catch (RepositoryException e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void loadNoExistentSupportedRepositoryTest() {
        //Arrange

        //Act
        try {
            SupportedRepository supportedRepo = Util.getRepositoriesSupportedData("Github1");

            //Assert
            assertNull(supportedRepo);

        } catch (RepositoryException e) {
            fail("Should not have thrown any exception");
        }
    }
}
