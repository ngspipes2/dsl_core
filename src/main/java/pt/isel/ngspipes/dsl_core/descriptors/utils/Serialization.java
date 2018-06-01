package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;

import java.io.IOException;

public class Serialization {

    public enum Format {
        JSON,
        YAML
    }



    public static String serialize(Object obj, Format format) throws DSLCoreException {
        return serialize(obj, format, null);
    }

    public static String serialize(Object obj, Format format, SimpleAbstractTypeResolver resolver) throws DSLCoreException {
        if(format.equals(Format.JSON))
            return serialize(obj, JacksonUtils.getJSONMapper(resolver));

        if(format.equals(Format.YAML))
            return serialize(obj, JacksonUtils.getYAMLMapper(resolver));

        throw new DSLCoreException("Unknown Format:" + format);
    }

    private static String serialize(Object obj, ObjectMapper mapper) throws DSLCoreException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new DSLCoreException("Error serializing Object!", e);
        }
    }


    public static <T> T deserialize(String content, Format format, JavaType klass) throws DSLCoreException {
        return deserialize(content, format, klass, null);
    }

    public static <T> T deserialize(String content, Format format, JavaType klass, SimpleAbstractTypeResolver resolver) throws DSLCoreException {
        if(format.equals(Format.JSON))
            return deserialize(content, klass, JacksonUtils.getJSONMapper(resolver));

        if(format.equals(Format.YAML))
            return deserialize(content, klass, JacksonUtils.getYAMLMapper(resolver));

        throw new DSLCoreException("Unknown Format:" + format);
    }

    public static <T> T deserialize(String content, JavaType klass, ObjectMapper mapper) throws DSLCoreException {
        try {
            return mapper.readValue(content, klass);
        } catch (IOException e) {
            throw new DSLCoreException("Error deserialize Object!", e);
        }
    }


    public static Format getFormatFromHttpHeader(String httpHeader) throws DSLCoreException {
        switch (httpHeader) {
            case "application/json": return Format.JSON;
            case "application/x-yaml": return Format.YAML;
        }

        throw new DSLCoreException("Unknown file http header:" + httpHeader);
    }

    public static String getHttpHeaderFromFormat(Format format) throws DSLCoreException {
        switch (format) {
            case JSON: return "application/json";
            case YAML: return "application/x-yaml";
        }

        throw new DSLCoreException("Unknown Format:" + format);
    }


    public static Format getFormatFromFileExtension(String fileExtension) throws DSLCoreException {
        switch (fileExtension) {
            case "json": return Format.JSON;
            case "yaml": return Format.YAML;
            case "yml": return Format.YAML;
        }

        throw new DSLCoreException("Unknown file extension:" + fileExtension);
    }

    public static String getFileExtensionFromFormat(Format format) throws  DSLCoreException {
        switch (format) {
            case JSON: return "json";
            case YAML: return "yaml";
        }

        throw new DSLCoreException("Unknown Format:" + format);
    }

}
