package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.input;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IChainInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IParameterInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.ISimpleInputDescriptor;

import java.io.IOException;

public class InputDescriptorDeserializer extends JsonDeserializer<IInputDescriptor> {

    public static final String TYPE_FIELD_NAME = InputDescriptorSerializer.TYPE_FIELD_NAME;
    public static final String SIMPLE_INPUT_DESCRIPTOR_TYPE = InputDescriptorSerializer.SIMPLE_INPUT_DESCRIPTOR_TYPE;
    public static final String PARAMETER_INPUT_DESCRIPTOR_TYPE = InputDescriptorSerializer.PARAMETER_INPUT_DESCRIPTOR_TYPE;
    public static final String CHAIN_INPUT_DESCRIPTOR_TYPE = InputDescriptorSerializer.CHAIN_INPUT_DESCRIPTOR_TYPE;



    @Override
    public IInputDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        if(!node.has(TYPE_FIELD_NAME))
            throw new JsonParseException(p, "No type field found!");

        String type = node.get(TYPE_FIELD_NAME).textValue();

        if(type != null) {
            if (type.equals(SIMPLE_INPUT_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), ISimpleInputDescriptor.class);
            if (type.equals(PARAMETER_INPUT_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IParameterInputDescriptor.class);
            if (type.equals(CHAIN_INPUT_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IChainInputDescriptor.class);
        }

        throw new JsonParseException(p, "Unknown InputDescriptor type:" + type);
    }

}
