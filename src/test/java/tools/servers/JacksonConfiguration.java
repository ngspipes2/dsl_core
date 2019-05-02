package tools.servers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.tool.ToolMapper;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return ToolMapper.getToolsMapper(new JsonFactory());
    }

}
