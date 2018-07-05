package pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.dsl_parser.domain.NGSPipesParser;
import pt.isel.ngspipes.dsl_parser.transversal.ParserException;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

public class PipelinesDescriptorUtils {

    public static IPipelineDescriptor createPipelineDescriptor(String content, PipelineSerialization.Format format) throws PipelinesRepositoryException {
        if(format.equals(PipelineSerialization.Format.PIPES)) {
            try {
                NGSPipesParser parser = new NGSPipesParser();
                return parser.getFromString(content);
            } catch (ParserException e) {
                throw new PipelinesRepositoryException(e.getMessage(), e);
            }
        } else {
            Serialization.Format fmt = PipelineSerialization.pipelineSerializationFormatToSerializationFormat(format);
            return createPipelineDescriptor(content, fmt);
        }
    }

    public static IPipelineDescriptor createPipelineDescriptor(String content, Serialization.Format format) throws PipelinesRepositoryException {
        try {
            JavaType klass = new ObjectMapper().getTypeFactory().constructType(TypedPipelineDescriptor.class);
            TypedPipelineDescriptor typedPipeline = Serialization.deserialize(content, format, klass);
            return JacksonEntityService.transformToIPipelineDescriptor(typedPipeline);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException("Error deserialize TypedPipelineDescriptor!", e);
        }
    }


    public static String getPipelineDescriptorAsString(IPipelineDescriptor pipeline, PipelineSerialization.Format format) throws PipelinesRepositoryException {
        if(format.equals(PipelineSerialization.Format.PIPES)) {
            try {
                NGSPipesParser parser = new NGSPipesParser();
                return parser.getAsString(pipeline);
            } catch(ParserException e) {
                throw new PipelinesRepositoryException(e.getMessage(), e);
            }
        } else {
            Serialization.Format fmt = PipelineSerialization.pipelineSerializationFormatToSerializationFormat(format);
            return getPipelineDescriptorAsString(pipeline, fmt);
        }
    }

    public static String getPipelineDescriptorAsString(TypedPipelineDescriptor typedPipelineDescriptor, PipelineSerialization.Format format) throws PipelinesRepositoryException {
        if(format.equals(PipelineSerialization.Format.PIPES)) {
            IPipelineDescriptor pipeline = JacksonEntityService.transformToIPipelineDescriptor(typedPipelineDescriptor);

            try {
                NGSPipesParser parser = new NGSPipesParser();
                return parser.getAsString(pipeline);
            } catch(ParserException e) {
                throw new PipelinesRepositoryException(e.getMessage(), e);
            }
        } else {
            Serialization.Format fmt = PipelineSerialization.pipelineSerializationFormatToSerializationFormat(format);
            return getPipelineDescriptorAsString(typedPipelineDescriptor, fmt);
        }
    }

    public static String getPipelineDescriptorAsString(IPipelineDescriptor pipeline, Serialization.Format format) throws PipelinesRepositoryException {
        TypedPipelineDescriptor typedPipeline = JacksonEntityService.transformToTypedPipelineDescriptor(pipeline);
        return getPipelineDescriptorAsString(typedPipeline, format);
    }

    public static String getPipelineDescriptorAsString(TypedPipelineDescriptor pipeline, Serialization.Format format) throws PipelinesRepositoryException {
        try {
            return Serialization.serialize(pipeline, format);
        }catch (DSLCoreException e) {
            throw new PipelinesRepositoryException("Error serializing TypedPipelineDescriptor!", e);
        }
    }

}
