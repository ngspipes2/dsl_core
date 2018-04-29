package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.entities.JacksonCommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.execution_context.ContainerExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.execution_context.LocalExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.CommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.configurator.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IOutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IToolDescriptor;

import java.io.IOException;

public class ToolsDescriptorsFactoryUtils {

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

    public static IExecutionContextDescriptor getExecutionContextDescriptor(String content, String type, String contextType) throws IOException {
        boolean local = contextType.equals("Local");
        if(type.equals("json"))
            return local ? getLocalExecutionContextForJsonDescriptor(content) : getContainerExecutionContextForJsonDescriptor(content);
        return local ? getLocalExecutionContextForYAMLDescriptor(content) : getContainerExecutionContextForYAMLDescriptor(content);
    }



    private static IExecutionContextDescriptor getLocalExecutionContextForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new JsonFactory()).readValue(content, LocalExecutionContextDescriptor.class);
    }

    private static IExecutionContextDescriptor getLocalExecutionContextForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, LocalExecutionContextDescriptor.class);
    }

    private static IExecutionContextDescriptor getContainerExecutionContextForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, ContainerExecutionContextDescriptor.class);
    }

    private static IExecutionContextDescriptor getContainerExecutionContextForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new JsonFactory()).readValue(content, ContainerExecutionContextDescriptor.class);
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

    private static ObjectMapper getObjectMapper(JsonFactory factory) {
        ObjectMapper objectMapper = new ObjectMapper(factory);

        SimpleModule module = new SimpleModule("CustomModel", Version.unknownVersion());

        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ICommandDescriptor.class, JacksonCommandDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, ParameterDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);

        module.setAbstractTypes(resolver);
        objectMapper.registerModule(module);

        return  objectMapper;
    }
}
