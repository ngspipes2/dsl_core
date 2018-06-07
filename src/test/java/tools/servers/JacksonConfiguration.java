package tools.servers;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.isel.ngspipes.tool_descriptor.implementations.*;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        SimpleAbstractTypeResolver resolver = getToolResolver();
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule("CustomModel", Version.unknownVersion());
        module.setAbstractTypes(resolver);
        mapper.registerModule(module);

        return mapper;
    }

    private static SimpleAbstractTypeResolver getToolResolver() {
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        resolver.addMapping(IToolDescriptor.class, ToolDescriptor.class);
        resolver.addMapping(ICommandDescriptor.class, CommandDescriptor.class);
        resolver.addMapping(IParameterDescriptor.class, ParameterDescriptor.class);
        resolver.addMapping(IOutputDescriptor.class, OutputDescriptor.class);
        resolver.addMapping(IExecutionContextDescriptor.class, ExecutionContextDescriptor.class);

        return resolver;
    }

}
