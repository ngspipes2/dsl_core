package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities.JacksonCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities.JacksonParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IOutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.io.IOException;

public class ToolsDescriptorsFactoryUtils {

    public static IToolDescriptor getToolDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getToolForJsonDescriptor(content);
        return getToolForYAMLDescriptor(content);
    }

    public static IExecutionContextDescriptor getExecutionContextDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getExecutionContextForJsonDescriptor(content);
        return getExecutionContextForYAMLDescriptor(content);
    }



    private static IExecutionContextDescriptor getExecutionContextForJsonDescriptor(String content) throws IOException {
        return getObjectMapper(new JsonFactory()).readValue(content, ExecutionContextDescriptor.class);
    }

    private static IExecutionContextDescriptor getExecutionContextForYAMLDescriptor(String content) throws IOException {
        return getObjectMapper(new YAMLFactory()).readValue(content, ExecutionContextDescriptor.class);
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
        resolver.addMapping(IParameterDescriptor.class, JacksonParameterDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        module.setAbstractTypes(resolver);
        objectMapper.registerModule(module);

        return  objectMapper;
    }

}
