package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.input;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IChainInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IParameterInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.ISimpleInputDescriptor;

import java.io.IOException;

public class InputDescriptorSerializer extends JsonSerializer<IInputDescriptor> {

    public static final String TYPE_FIELD_NAME = "type";
    public static final String SIMPLE_INPUT_DESCRIPTOR_TYPE = "Simple";
    public static final String PARAMETER_INPUT_DESCRIPTOR_TYPE = "Parameter";
    public static final String CHAIN_INPUT_DESCRIPTOR_TYPE = "Chain";



    @Override
    public void serialize(IInputDescriptor descriptor, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Class<?> klass;

        if(descriptor instanceof ISimpleInputDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, SIMPLE_INPUT_DESCRIPTOR_TYPE);
            klass = ISimpleInputDescriptor.class;
        } else if(descriptor instanceof IParameterInputDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, PARAMETER_INPUT_DESCRIPTOR_TYPE);
            klass = IParameterInputDescriptor.class;
        } else if(descriptor instanceof IChainInputDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, CHAIN_INPUT_DESCRIPTOR_TYPE);
            klass = IChainInputDescriptor.class;
        } else
            throw new IOException("Unknown type of InputDescriptor:" + descriptor.getClass().getName());

        writeAllFields(descriptor, gen, provider, klass);

        gen.writeEndObject();
    }

    private void writeAllFields(IInputDescriptor descriptor, JsonGenerator gen, SerializerProvider provider, Class<?> klass) throws IOException {
        JavaType javaType = provider.constructType(klass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);
        serializer.unwrappingSerializer(null).serialize(descriptor, gen, provider);
    }

}
