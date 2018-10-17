package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.exec;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.ICommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IPipelineExecDescriptor;

import java.io.IOException;

public class ExecDescriptorDeserializer extends JsonDeserializer<IExecDescriptor> {

    public static final String TYPE_FIELD_NAME = ExecDescriptorSerializer.TYPE_FIELD_NAME;
    public static final String COMMAND_EXEC_DESCRIPTOR_TYPE = ExecDescriptorSerializer.COMMAND_EXEC_DESCRIPTOR_TYPE;
    public static final String PIPELINE_EXEC_DESCRIPTOR_TYPE = ExecDescriptorSerializer.PIPELINE_EXEC_DESCRIPTOR_TYPE;



    @Override
    public IExecDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        if(!node.has(TYPE_FIELD_NAME))
            throw new JsonParseException(p, "No type field found!");

        String type = node.get(TYPE_FIELD_NAME).textValue();

        if(type != null) {
            if(type.equals(COMMAND_EXEC_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), ICommandExecDescriptor.class);
            else if(type.equals(PIPELINE_EXEC_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IPipelineExecDescriptor.class);
        }

        throw new JsonParseException(p, "Unknown ExecDescriptor type:" + type);
    }

}
