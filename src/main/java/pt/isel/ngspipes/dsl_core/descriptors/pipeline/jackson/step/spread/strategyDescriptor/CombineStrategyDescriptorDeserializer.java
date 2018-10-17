package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.spread.strategyDescriptor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.ICombineStrategyDescriptor;

import java.io.IOException;

public class CombineStrategyDescriptorDeserializer extends JsonDeserializer<ICombineStrategyDescriptor> {

    @Override
    public ICombineStrategyDescriptor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return (ICombineStrategyDescriptor) new StrategyDescriptorDeserializer().deserialize(p, ctxt);
    }

}
