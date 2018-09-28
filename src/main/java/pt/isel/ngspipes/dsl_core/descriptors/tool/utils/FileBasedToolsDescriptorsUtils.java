package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.fileBased.FileBasedCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.fileBased.FileBasedParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.fileBased.FileBasedToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.tool_descriptor.implementations.ExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;
import utils.ToolsRepositoryException;

public class FileBasedToolsDescriptorsUtils {

    public static IToolDescriptor createToolDescriptor(String content, String fileExtension) throws ToolsRepositoryException {
        Serialization.Format format;

        try {
            format = Serialization.getFormatFromFileExtension(fileExtension);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return createToolDescriptor(content, format);
    }

    public static IToolDescriptor createToolDescriptor(String content, Serialization.Format format) throws ToolsRepositoryException {
        try {
            JavaType klass = new ObjectMapper().getTypeFactory().constructType(ToolDescriptor.class);
            return Serialization.deserialize(content, format, klass, getToolResolver());
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error deserialize ToolDescriptor!", e);
        }
    }

    public static IExecutionContextDescriptor createExecutionContextDescriptor(String content, String fileExtension) throws ToolsRepositoryException {
        Serialization.Format format;

        try {
            format = Serialization.getFormatFromFileExtension(fileExtension);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return createExecutionContextDescriptor(content, format);
    }

    public static IExecutionContextDescriptor createExecutionContextDescriptor(String content, Serialization.Format format) throws ToolsRepositoryException {
        try {
            JavaType klass = new ObjectMapper().getTypeFactory().constructType(ExecutionContextDescriptor.class);
            return Serialization.deserialize(content, format, klass, getExecutionContextResolver());
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error deserialize ExecutionContext!", e);
        }
    }


    public static String getToolDescriptorAsString(IToolDescriptor tool, String fileExtension) throws ToolsRepositoryException {
        Serialization.Format format;

        try {
            format = Serialization.getFormatFromFileExtension(fileExtension);
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return getToolDescriptorAsString(tool, format);
    }

    public static String getToolDescriptorAsString(IToolDescriptor tool, Serialization.Format format) throws ToolsRepositoryException {
        try {
            return Serialization.serialize(tool, format, getToolResolver());
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error serializing ToolDescriptor!", e);
        }
    }

    public static String getExecutionContextDescriptorAsString(IExecutionContextDescriptor ctx, String fileExtension) throws ToolsRepositoryException {
        Serialization.Format format;

        try {
            format = Serialization.getFormatFromFileExtension(fileExtension);
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }

        return getExecutionContextDescriptorAsString(ctx, format);
    }

    public static String getExecutionContextDescriptorAsString(IExecutionContextDescriptor ctx, Serialization.Format format) throws ToolsRepositoryException {
        try {
            return Serialization.serialize(ctx, format, getExecutionContextResolver());
        }catch (DSLCoreException e) {
            throw new ToolsRepositoryException("Error serializing ExecutionContext!", e);
        }
    }


    private static SimpleAbstractTypeResolver getToolResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IToolDescriptor.class, FileBasedToolDescriptor.class);
        resolver.addMapping(ICommandDescriptor.class, FileBasedCommandDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, FileBasedParameterDescriptor.class);
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
