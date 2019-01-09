package pt.isel.ngspipes.dsl_core.descriptors.tool;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import pt.isel.ngspipes.dsl_core.descriptors.utils.JacksonUtils;
import pt.isel.ngspipes.tool_descriptor.implementations.*;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;

public class ToolMapper {

    public static ObjectMapper getToolsMapper(JsonFactory factory) {
        SimpleAbstractTypeResolver resolver = getTypeResolver();
        ObjectMapper mapper = JacksonUtils.getObjectMapper(factory, resolver);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }

    public static SimpleAbstractTypeResolver getTypeResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IToolDescriptor.class, ToolDescriptor.class);
        resolver.addMapping(ICommandDescriptor.class, CommandDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, ParameterDescriptor.class);
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        return resolver;
    }

}
