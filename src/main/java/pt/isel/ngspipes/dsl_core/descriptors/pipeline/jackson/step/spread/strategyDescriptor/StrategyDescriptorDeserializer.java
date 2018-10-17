package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.spread.strategyDescriptor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IInputStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IOneToManyStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IOneToOneStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IStrategyDescriptor;

import java.io.IOException;

public class StrategyDescriptorDeserializer extends JsonDeserializer<IStrategyDescriptor> {

    public static final String TYPE_FIELD_NAME = StrategyDescriptorSerializer.TYPE_FIELD_NAME;
    public static final String INPUT_STRATEGY_DESCRIPTOR_TYPE = StrategyDescriptorSerializer.INPUT_STRATEGY_DESCRIPTOR_TYPE;
    public static final String ONE_TO_ONE_STRATEGY_DESCRIPTOR_TYPE = StrategyDescriptorSerializer.ONE_TO_ONE_STRATEGY_DESCRIPTOR_TYPE;
    public static final String ONE_TO_MANY_STRATEGY_DESCRIPTOR_TYPE = StrategyDescriptorSerializer.ONE_TO_MANY_STRATEGY_DESCRIPTOR_TYPE;



    @Override
    public IStrategyDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        if(!node.has(TYPE_FIELD_NAME))
            throw new JsonParseException(p, "No type field found!");

        String type = node.get(TYPE_FIELD_NAME).textValue();

        if(type != null) {
            if (type.equals(INPUT_STRATEGY_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IInputStrategyDescriptor.class);
            if (type.equals(ONE_TO_ONE_STRATEGY_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IOneToOneStrategyDescriptor.class);
            if (type.equals(ONE_TO_MANY_STRATEGY_DESCRIPTOR_TYPE))
                return mapper.readValue(node.toString(), IOneToManyStrategyDescriptor.class);
        }

        throw new JsonParseException(p, "Unknown StrategyDescriptor type:" + type);
    }

}
