package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.step.exec;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.ICommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IPipelineExecDescriptor;

import java.io.IOException;

public class ExecDescriptorSerializer extends JsonSerializer<IExecDescriptor> {

    public static final String TYPE_FIELD_NAME = "type";
    public static final String COMMAND_EXEC_DESCRIPTOR_TYPE = "Command";
    public static final String PIPELINE_EXEC_DESCRIPTOR_TYPE = "Pipeline";



    @Override
    public void serialize(IExecDescriptor descriptor, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Class<?> klass;

        if(descriptor instanceof ICommandExecDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, COMMAND_EXEC_DESCRIPTOR_TYPE);
            klass = ICommandExecDescriptor.class;
        } else if(descriptor instanceof IPipelineExecDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, PIPELINE_EXEC_DESCRIPTOR_TYPE);
            klass = IPipelineExecDescriptor.class;
        } else
            throw new IOException("Unknown type of ExecDescriptor:" + descriptor.getClass().getName());

        writeAllFields(descriptor, gen, provider, klass);

        gen.writeEndObject();
    }

    private void writeAllFields(IExecDescriptor descriptor, JsonGenerator gen, SerializerProvider provider, Class<?> klass) throws IOException {
        JavaType javaType = provider.constructType(klass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);
        serializer.unwrappingSerializer(null).serialize(descriptor, gen, provider);
    }

}
