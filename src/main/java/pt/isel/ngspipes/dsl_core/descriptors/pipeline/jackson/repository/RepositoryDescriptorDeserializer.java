package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.pipeline_descriptor.repository.IPipelineRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IToolRepositoryDescriptor;

import java.io.IOException;

public class RepositoryDescriptorDeserializer extends JsonDeserializer<IRepositoryDescriptor> {

    public static final String TYPE_FIELD_NAME = RepositoryDescriptorSerializer.TYPE_FIELD_NAME;
    public static final String TOOLS_REPOSITORY_DESCRIPTOR_TYPE = RepositoryDescriptorSerializer.TOOLS_REPOSITORY_DESCRIPTOR_TYPE;
    public static final String PIPELINES_REPOSITORY_DESCRIPTOR_TYPE = RepositoryDescriptorSerializer.PIPELINES_REPOSITORY_DESCRIPTOR_TYPE;



    @Override
    public IRepositoryDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        if(!node.has(TYPE_FIELD_NAME))
            throw new JsonParseException(p, "No type field found!");

        String type = node.get(TYPE_FIELD_NAME).textValue();

        if(type != null) {
            if(type.equals(TOOLS_REPOSITORY_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IToolRepositoryDescriptor.class);
            if(type.equals(PIPELINES_REPOSITORY_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IPipelineRepositoryDescriptor.class);
        }

        throw new JsonParseException(p, "Unknown RepositoryDescriptor type:" + type);
    }

}
