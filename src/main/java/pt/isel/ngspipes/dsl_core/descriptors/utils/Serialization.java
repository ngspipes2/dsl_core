package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
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

    public static String serialize(Object obj, Class<?> klass, Format format) throws DSLCoreException {
        return serialize(obj, klass, format, null);
    }

    public static String serialize(Object obj, Format format, SimpleAbstractTypeResolver resolver) throws DSLCoreException {
        return serialize(obj, obj.getClass(), format, resolver);
    }

    public static String serialize(Object obj, Class<?> klass, Format format, SimpleAbstractTypeResolver resolver) throws DSLCoreException {
        if(format.equals(Format.JSON))
            return serialize(obj, klass, JacksonUtils.getJSONMapper(resolver));

        if(format.equals(Format.YAML))
            return serialize(obj, klass, JacksonUtils.getYAMLMapper(resolver));

        throw new DSLCoreException("Unknown Format:" + format);
    }

    public static String serialize(Object obj, ObjectMapper mapper) throws DSLCoreException {
        return serialize(obj, obj.getClass(), mapper);
    }

    public static String serialize(Object obj, Class<?> klass,  ObjectMapper mapper) throws DSLCoreException {
        return serialize(obj, mapper.writerFor(klass));
    }

    public static String serialize(Object obj, ObjectWriter writer) throws DSLCoreException {
        try {
            return writer.writeValueAsString(obj);
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
        return deserialize(content, mapper.readerFor(klass));
    }

    public static <T> T deserialize(String content, ObjectReader reader) throws DSLCoreException {
        try {
            return reader.readValue(content);
        } catch (IOException e) {
            throw new DSLCoreException("Error deserialize Object!", e);
        }
    }


    public static Format getFormatFromHttpHeader(String httpHeader) throws DSLCoreException {
        if(httpHeader.contains("application/json"))
            return Format.JSON;

        if(httpHeader.contains("application/x-yaml"))
            return Format.YAML;

        throw new DSLCoreException("Unknown http header:" + httpHeader);
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
