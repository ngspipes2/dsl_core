package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonUtils {


    public static ObjectMapper getObjectMapper(JsonFactory factory) {
        return getObjectMapper(factory, null);
    }

    public static ObjectMapper getObjectMapper(JsonFactory factory, SimpleAbstractTypeResolver resolver) {
        ObjectMapper objectMapper = new ObjectMapper(factory);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        if(resolver != null) {
            SimpleModule module = new SimpleModule("CustomModel", Version.unknownVersion());
            module.setAbstractTypes(resolver);
            objectMapper.registerModule(module);
        }

        return  objectMapper;
    }

}
