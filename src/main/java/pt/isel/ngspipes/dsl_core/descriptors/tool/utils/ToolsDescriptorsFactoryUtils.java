package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities.JacksonCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities.JacksonParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities.JacksonToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;

import java.io.IOException;

public class ToolsDescriptorsFactoryUtils {

    public static IToolDescriptor createToolDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getToolForJsonDescriptor(content);
        return getToolForYAMLDescriptor(content);
    }

    public static IExecutionContextDescriptor createExecutionContextDescriptor(String content, String type) throws IOException {
        if(type.equals("json"))
            return getExecutionContextForJsonDescriptor(content);
        return getExecutionContextForYAMLDescriptor(content);
    }

    public static String getToolDescriptorAsString(IToolDescriptor tool, String type) throws JsonProcessingException {
        ObjectMapper mapper;
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(IToolDescriptor.class, JacksonToolDescriptor.class);

        if(type.equals("json"))
            mapper = JacksonUtils.getObjectMapper(new JsonFactory(), resolver);
        else
            mapper = JacksonUtils.getObjectMapper(new YAMLFactory(), resolver);

        return mapper.writeValueAsString(tool);
    }

    public static String getExecutionContextDescriptorAsString(IExecutionContextDescriptor ctx, String type) throws JsonProcessingException {
        ObjectMapper mapper;
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        if(type.equals("json"))
            mapper = JacksonUtils.getObjectMapper(new JsonFactory(), resolver);
        else
            mapper = JacksonUtils.getObjectMapper(new YAMLFactory(), resolver);

        return mapper.writeValueAsString(ctx);
    }



    private static SimpleAbstractTypeResolver getToolResolver() {

        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();
        resolver.addMapping(ICommandDescriptor.class, JacksonCommandDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, JacksonParameterDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);
        return resolver;
    }

    private static IExecutionContextDescriptor getExecutionContextForJsonDescriptor(String content) throws IOException {
        return JacksonUtils.getObjectMapper(new JsonFactory()).readValue(content, ExecutionContextDescriptor.class);
    }

    private static IExecutionContextDescriptor getExecutionContextForYAMLDescriptor(String content) throws IOException {
        return JacksonUtils.getObjectMapper(new YAMLFactory()).readValue(content, ExecutionContextDescriptor.class);
    }



    private static IToolDescriptor getToolForYAMLDescriptor(String content) throws IOException {
        return JacksonUtils.getObjectMapper(new YAMLFactory(), getToolResolver()).readValue(content, ToolDescriptor.class);
    }

    private static IToolDescriptor getToolForJsonDescriptor(String content) throws IOException {
        return JacksonUtils.getObjectMapper(new JsonFactory(), getToolResolver()).readValue(content, ToolDescriptor.class);
    }
}
