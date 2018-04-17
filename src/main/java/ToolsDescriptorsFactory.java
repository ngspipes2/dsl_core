import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.CommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IOutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IToolDescriptor;

import java.io.IOException;

public class ToolsDescriptorsFactory {

    public static IToolDescriptor getToolDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getToolForJsonDescriptor(content);
        return getToolForYAMLDescriptor(content);
    }

    public static ICommandDescriptor getCommandDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getCommandForJsonDescriptor(content);
        return getCommandForYAMLDescriptor(content);
    }

    public static IParameterDescriptor getParameterDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getParameterForJsonDescriptor(content);
        return getParameterForYAMLDescriptor(content);
    }

    public static IOutputDescriptor getOutputDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getOutputForJsonDescriptor(content);
        return getOutputForYAMLDescriptor(content);
    }

    private static IOutputDescriptor getOutputForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, OutputDescriptor.class);
    }

    private static IOutputDescriptor getOutputForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new JsonFactory()).readValue(content, OutputDescriptor.class);
    }

    private static IParameterDescriptor getParameterForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, ParameterDescriptor.class);
    }

    private static IParameterDescriptor getParameterForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, ParameterDescriptor.class);
    }

    private static ICommandDescriptor getCommandForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, CommandDescriptor.class);
    }

    private static ICommandDescriptor getCommandForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new JsonFactory()).readValue(content, CommandDescriptor.class);
    }

    private static IToolDescriptor getToolForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, ToolDescriptor.class);
    }

    private static IToolDescriptor getToolForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new JsonFactory()).readValue(content, ToolDescriptor.class);
    }

    private static ObjectMapper getObjectMapper(JsonFactory factory) { return new ObjectMapper(factory); }
}
