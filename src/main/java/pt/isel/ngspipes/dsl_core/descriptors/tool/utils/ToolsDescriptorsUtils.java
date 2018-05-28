package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.utils.SerializationUtils;
import pt.isel.ngspipes.tool_descriptor.implementations.ExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;
import utils.ToolsRepositoryException;

public class ToolsDescriptorsUtils {

    public static IToolDescriptor createToolDescriptor(String content, String fileExtension) throws ToolsRepositoryException {
        SerializationUtils.SerializationFormat format;

        try {
            format = SerializationUtils.getFormatFromFileExtension(fileExtension);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return createToolDescriptor(content, format);
    }

    public static IToolDescriptor createToolDescriptor(String content, SerializationUtils.SerializationFormat format) throws ToolsRepositoryException {
        try {
            JavaType klass = new ObjectMapper().getTypeFactory().constructType(ToolDescriptor.class);
            return SerializationUtils.deserialize(content, format, klass, getToolResolver());
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error deserialize ToolDescriptor!", e);
        }
    }

    public static IExecutionContextDescriptor createExecutionContextDescriptor(String content, String fileExtension) throws ToolsRepositoryException {
        SerializationUtils.SerializationFormat format;

        try {
            format = SerializationUtils.getFormatFromFileExtension(fileExtension);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return createExecutionContextDescriptor(content, format);
    }

    public static IExecutionContextDescriptor createExecutionContextDescriptor(String content, SerializationUtils.SerializationFormat format) throws ToolsRepositoryException {
        try {
            JavaType klass = new ObjectMapper().getTypeFactory().constructType(ExecutionContextDescriptor.class);
            return SerializationUtils.deserialize(content, format, klass, getExecutionContextResolver());
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error deserialize ExecutionContext!", e);
        }
    }


    public static String getToolDescriptorAsString(IToolDescriptor tool, String fileExtension) throws ToolsRepositoryException {
        SerializationUtils.SerializationFormat format;

        try {
            format = SerializationUtils.getFormatFromFileExtension(fileExtension);
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return getToolDescriptorAsString(tool, format);
    }

    public static String getToolDescriptorAsString(IToolDescriptor tool, SerializationUtils.SerializationFormat format) throws ToolsRepositoryException {
        try {
            return SerializationUtils.serialize(tool, format, getToolResolver());
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error serializing ToolDescriptor!", e);
        }
    }

    public static String getExecutionContextDescriptorAsString(IExecutionContextDescriptor ctx, String fileExtension) throws ToolsRepositoryException {
        SerializationUtils.SerializationFormat format;

        try {
            format = SerializationUtils.getFormatFromFileExtension(fileExtension);
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return getExecutionContextDescriptorAsString(ctx, format);
    }

    public static String getExecutionContextDescriptorAsString(IExecutionContextDescriptor ctx, SerializationUtils.SerializationFormat format) throws ToolsRepositoryException {
        try {
            return SerializationUtils.serialize(ctx, format, getExecutionContextResolver());
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error serializing ExecutionContext!", e);
        }
    }


    private static SimpleAbstractTypeResolver getToolResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IToolDescriptor.class, JacksonToolDescriptor.class);
        resolver.addMapping(ICommandDescriptor.class, JacksonCommandDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, JacksonParameterDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        return resolver;
    }

    private static SimpleAbstractTypeResolver getExecutionContextResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        return resolver;
    }

}
