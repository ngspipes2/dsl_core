package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class JacksonUtils {


    public static ObjectMapper getObjecTMapper() {
        return getJSONMapper();
    }

    public static ObjectMapper getObjectMapper(JsonFactory factory) {
        return getObjectMapper(factory, null);
    }

    public static ObjectMapper getObjectMapper(JsonFactory factory, SimpleAbstractTypeResolver resolver) {
        ObjectMapper objectMapper = new ObjectMapper(factory);

        initMapper(objectMapper, resolver);

        return objectMapper;
    }


    public static ObjectMapper getJSONMapper() {
        return getJSONMapper(null);
    }

    public static ObjectMapper getJSONMapper(SimpleAbstractTypeResolver resolver) {
        ObjectMapper objectMapper = new ObjectMapper();

        initMapper(objectMapper, resolver);

        return objectMapper;
    }


    public static ObjectMapper getYAMLMapper() {
        return getYAMLMapper(null);
    }

    public static ObjectMapper getYAMLMapper(SimpleAbstractTypeResolver resolver) {
        ObjectMapper objectMapper = new YAMLMapper();

        initMapper(objectMapper, resolver);

        return objectMapper;
    }


    private static void initMapper(ObjectMapper mapper, SimpleAbstractTypeResolver resolver) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        if(resolver != null) {
            SimpleModule module = new SimpleModule("CustomModel", Version.unknownVersion());
            module.setAbstractTypes(resolver);
            mapper.registerModule(module);
        }
    }

}
