package pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.PipelineMapper;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.fileBased.FileBasedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.dsl_parser.domain.NGSPipesParser;
import pt.isel.ngspipes.dsl_parser.transversal.ParserException;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

public class FileBasedPipelineDescriptorUtils {

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
            ObjectMapper mapper = PipelineMapper.getPipelinesMapper(format.getFactory());
            JavaType klass = mapper.getTypeFactory().constructType(FileBasedPipelineDescriptor.class);
            return  Serialization.deserialize(content, klass, mapper);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException("Error deserialize PipelineDescriptor!", e);
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

    public static String getPipelineDescriptorAsString(IPipelineDescriptor pipeline, Serialization.Format format) throws PipelinesRepositoryException {
        try {
            ObjectMapper mapper = PipelineMapper.getPipelinesMapper(format.getFactory());
            return Serialization.serialize(pipeline, mapper);
        }catch (DSLCoreException e) {
            throw new PipelinesRepositoryException("Error serializing PipelineDescriptor!", e);
        }
    }

}
