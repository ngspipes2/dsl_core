package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository.value;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IParameterValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.ISimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IValueDescriptor;

import java.io.IOException;

public class ValueDescriptorSerializer extends JsonSerializer<IValueDescriptor> {

    public static final String TYPE_FIELD_NAME = "type";
    public static final String PARAMETER_VALUE_DESCRIPTOR_TYPE = "Parameter";
    public static final String SIMPLE_VALUE_DESCRIPTOR_TYPE = "Simple";



    @Override
    public void serialize(IValueDescriptor descriptor, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Class<?> klass;

        if(descriptor instanceof ISimpleValueDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, SIMPLE_VALUE_DESCRIPTOR_TYPE);
            klass = ISimpleValueDescriptor.class;
        } else if(descriptor instanceof IParameterValueDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, PARAMETER_VALUE_DESCRIPTOR_TYPE);
            klass = IParameterValueDescriptor.class;
        } else
            throw new IOException("Unknown type of ValueDescriptor:" + descriptor.getClass().getName());

        writeAllFields(descriptor, gen, provider, klass);

        gen.writeEndObject();
    }

    private void writeAllFields(IValueDescriptor descriptor, JsonGenerator gen, SerializerProvider provider, Class<?> klass) throws IOException {
        JavaType javaType = provider.constructType(klass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);
        serializer.unwrappingSerializer(null).serialize(descriptor, gen, provider);
    }

}
