import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;

public class IOUtilsTests {

    private static final String LOCATION;



    static {
        LOCATION = ClassLoader.getSystemResource("tools_repo").getPath();
    }



    @Test
    public void loadDirectoryFilesName() {

        //Arrange
        String expectedName = "Descriptor.json";
        String path = LOCATION + "/Trimmomatic/";

        //Act
        Collection<String> names = IOUtils.getDirectoryFilesName(path);

        //Assert
        assertTrue(names.size()>0);
        assertTrue(names.contains(expectedName));
    }

    @Test
    public void loadSubDirectoriesName() {

        //Arrange
        String expectedName = "execution_contexts";
        String path = LOCATION + "/Trimmomatic/";

        //Act
        Collection<String> names = IOUtils.getSubDirectoriesName(path);

        //Assert
        assertTrue(names.size()>0);
        assertTrue(names.contains(expectedName));
    }
}
