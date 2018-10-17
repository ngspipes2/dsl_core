package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.spread.strategyDescriptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IInputStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IOneToManyStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IOneToOneStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IStrategyDescriptor;

import java.io.IOException;

public class StrategyDescriptorSerializer extends JsonSerializer<IStrategyDescriptor> {

    public static final String TYPE_FIELD_NAME = "type";
    public static final String INPUT_STRATEGY_DESCRIPTOR_TYPE = "Input";
    public static final String ONE_TO_ONE_STRATEGY_DESCRIPTOR_TYPE = "OneToOne";
    public static final String ONE_TO_MANY_STRATEGY_DESCRIPTOR_TYPE = "OneToMany";



    @Override
    public void serialize(IStrategyDescriptor descriptor, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Class<?> klass;

        if(descriptor instanceof IInputStrategyDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, INPUT_STRATEGY_DESCRIPTOR_TYPE);
            klass = IInputStrategyDescriptor.class;
        } else if(descriptor instanceof IOneToOneStrategyDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, ONE_TO_ONE_STRATEGY_DESCRIPTOR_TYPE);
            klass = IOneToOneStrategyDescriptor.class;
        } else if(descriptor instanceof IOneToManyStrategyDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, ONE_TO_MANY_STRATEGY_DESCRIPTOR_TYPE);
            klass = IOneToManyStrategyDescriptor.class;
        } else
            throw new IOException("Unknown type of StrategyDescriptor:" + descriptor.getClass().getName());

        writeAllFields(descriptor, gen, provider, klass);

        gen.writeEndObject();
    }

    private void writeAllFields(IStrategyDescriptor descriptor, JsonGenerator gen, SerializerProvider provider, Class<?> klass) throws IOException {
        JavaType javaType = provider.constructType(klass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);
        serializer.unwrappingSerializer(null).serialize(descriptor, gen, provider);
    }

}
