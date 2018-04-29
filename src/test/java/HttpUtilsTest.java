import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.HttpUtils;

import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HttpUtilsTest {

    private static final String TEST_GET_TOOLS_NAME_URI = "https://api.github.com/repos/ngspipes2/tools_support/contents";
    private final String TEST_GET_JSON_NODE_ARRAY_URI = "https://api.github.com/repos/ngspipes2/tools_support/contents";
    private final String TEST_GET_JSON_NODE_URI = "https://raw.githubusercontent.com/ngspipes2/tools_support/master/Blast/Descriptor.json";

    @Test
    public void getJsonNodeTest() {
        //Arrange
        String fieldName = "name";
        String nameValue = "Blast";

        //Act
        JsonNode node = HttpUtils.getJsonNodeFrom(TEST_GET_JSON_NODE_URI);

        //Assert
        assertNotNull(node);
        assertTrue(node.has(fieldName));
        assertTrue(node.get(fieldName).asText().equals(nameValue));
    }

    @Test
    public void getJsonNodeArrayTest() {
        //Arrange

        //Act
        JsonNode node = HttpUtils.getJsonNodeFrom(TEST_GET_JSON_NODE_ARRAY_URI);

        //Assert
        assertTrue(node.isArray());

    }

    @Test
    public void getJsonNodeFieldTest() {
        //Arrange
        String fieldName = "commands";

        //Act
        JsonNode node = HttpUtils.getJsonFieldFromNode(TEST_GET_JSON_NODE_URI, fieldName);

        //Assert
        assertNotNull(node);
        assertTrue(node.isArray());
    }

    @Test
    public void getToolsNamesdTest() {
        //Arrange
        String fieldName = "name";
        String expectedValue = "Blast";

        //Act
        Collection<String> values = HttpUtils.getJsonFieldsValuesFromArray(TEST_GET_TOOLS_NAME_URI, fieldName);

        //Assert
        assertFalse(values.isEmpty());
        assertTrue(values.contains(expectedValue));
    }

}
