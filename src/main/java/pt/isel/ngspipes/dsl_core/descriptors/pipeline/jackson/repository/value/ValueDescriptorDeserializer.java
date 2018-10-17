package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository.value;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IParameterValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.ISimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IValueDescriptor;

import java.io.IOException;

public class ValueDescriptorDeserializer extends JsonDeserializer<IValueDescriptor> {

    public static final String TYPE_FIELD_NAME = ValueDescriptorSerializer.TYPE_FIELD_NAME;
    public static final String PARAMETER_VALUE_DESCRIPTOR_TYPE = ValueDescriptorSerializer.PARAMETER_VALUE_DESCRIPTOR_TYPE;
    public static final String SIMPLE_VALUE_DESCRIPTOR_TYPE = ValueDescriptorSerializer.SIMPLE_VALUE_DESCRIPTOR_TYPE;



    @Override
    public IValueDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        if(!node.has(TYPE_FIELD_NAME))
            throw new JsonParseException(p, "No type field found!");

        String type = node.get(TYPE_FIELD_NAME).textValue();

        if(type != null) {
            if(type.equals(PARAMETER_VALUE_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IParameterValueDescriptor.class);
            if(type.equals(SIMPLE_VALUE_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), ISimpleValueDescriptor.class);
        }

        throw new JsonParseException(p, "Unknown ValueDescriptor type:" + type);
    }

}
