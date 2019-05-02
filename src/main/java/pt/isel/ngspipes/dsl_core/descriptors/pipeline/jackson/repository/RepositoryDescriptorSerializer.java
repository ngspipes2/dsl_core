package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson.repository;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import pt.isel.ngspipes.pipeline_descriptor.repository.IPipelineRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IToolRepositoryDescriptor;

import java.io.IOException;

public class RepositoryDescriptorSerializer extends JsonSerializer<IRepositoryDescriptor> {

    public static final String TYPE_FIELD_NAME = "type";
    public static final String TOOLS_REPOSITORY_DESCRIPTOR_TYPE = "Tool";
    public static final String PIPELINES_REPOSITORY_DESCRIPTOR_TYPE = "Pipeline";



    @Override
    public void serialize(IRepositoryDescriptor descriptor, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        Class<?> klass;

        if(descriptor instanceof IToolRepositoryDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, TOOLS_REPOSITORY_DESCRIPTOR_TYPE);
            klass = IToolRepositoryDescriptor.class;
        } else if(descriptor instanceof IPipelineRepositoryDescriptor) {
            gen.writeStringField(TYPE_FIELD_NAME, PIPELINES_REPOSITORY_DESCRIPTOR_TYPE);
            klass = IPipelineRepositoryDescriptor.class;
        } else
            throw new IOException("Unknown type of RepositoryDescriptor:" + descriptor.getClass().getName());

        writeAllFields(descriptor, gen, provider, klass);

        gen.writeEndObject();
    }

    private void writeAllFields(IRepositoryDescriptor descriptor, JsonGenerator gen, SerializerProvider provider, Class<?> klass) throws IOException {
        JavaType javaType = provider.constructType(klass);
        BeanDescription beanDesc = provider.getConfig().introspect(javaType);
        JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);
        serializer.unwrappingSerializer(null).serialize(descriptor, gen, provider);
    }

}
