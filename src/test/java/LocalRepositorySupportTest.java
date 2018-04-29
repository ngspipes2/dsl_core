import implementations.LocalRepository;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LocalRepositorySupportTest {


    @Test
    public void localRepositorySupportTest() {
        //Arrange
        String location = "E:\\Repo";
        LocalRepository localRepository = new LocalRepository();

        // Act
        boolean isSupported = localRepository.load(location, null);

        //Assert
        assertTrue(isSupported);
    }

    @Test
    public void localRepositorySupportWithBadLocationTest() {
        //Arrange
        String location = "https://github1.com/ngspipes/tools";
        LocalRepository localRepository = new LocalRepository();

        // Act
        boolean isSupported = localRepository.load(location, null);

        //Assert
        assertFalse(isSupported);
    }
}
