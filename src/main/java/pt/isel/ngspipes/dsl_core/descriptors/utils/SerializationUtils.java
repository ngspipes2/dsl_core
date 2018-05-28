package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;

import java.io.IOException;

public class SerializationUtils {

    public enum SerializationFormat {
        JSON,
        XML,
        YAML
    }



    public static String serialize(Object obj, SerializationFormat serializationFormat) throws DSLCoreException {
        return serialize(obj, serializationFormat, null);
    }

    public static String serialize(Object obj, SerializationFormat serializationFormat, SimpleAbstractTypeResolver resolver) throws DSLCoreException {
        if(serializationFormat.equals(SerializationFormat.JSON))
            return serialize(obj, resolver, new JsonFactory());

        if(serializationFormat.equals(SerializationFormat.YAML))
            return serialize(obj, resolver, new YAMLFactory());

        if(serializationFormat.equals(SerializationFormat.XML))
            return serialize(obj, resolver, new XmlFactory());

        throw new DSLCoreException("Unknown SerializationFormat:" + serializationFormat);
    }

    private static String serialize(Object obj, SimpleAbstractTypeResolver resolver, JsonFactory factory) throws DSLCoreException {
        ObjectMapper mapper;

        if(resolver == null)
            mapper = JacksonUtils.getObjectMapper(factory);
        else
            mapper = JacksonUtils.getObjectMapper(factory, resolver);

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new DSLCoreException("Error serializing Object!", e);
        }
    }


    public static <T> T deserialize(String content, SerializationFormat serializationFormat, JavaType klass) throws DSLCoreException {
        return deserialize(content, serializationFormat, klass, null);
    }

    public static <T> T deserialize(String content, SerializationFormat serializationFormat, JavaType klass, SimpleAbstractTypeResolver resolver) throws DSLCoreException {
        if(serializationFormat.equals(SerializationFormat.JSON))
            return deserialize(content, klass, resolver, new JsonFactory());

        if(serializationFormat.equals(SerializationFormat.YAML))
            return deserialize(content, klass, resolver, new YAMLFactory());

        if(serializationFormat.equals(SerializationFormat.XML))
            return deserialize(content, klass, resolver, new XmlFactory());

        throw new DSLCoreException("Unknown SerializationFormat:" + serializationFormat);
    }

    public static <T> T deserialize(String content, JavaType klass, SimpleAbstractTypeResolver resolver, JsonFactory factory) throws DSLCoreException {
        ObjectMapper mapper;

        if(resolver == null)
            mapper = JacksonUtils.getObjectMapper(factory);
        else
            mapper = JacksonUtils.getObjectMapper(factory, resolver);

        try {
            return mapper.readValue(content, klass);
        } catch (IOException e) {
            throw new DSLCoreException("Error deserialize Object!", e);
        }
    }


    public static SerializationFormat getFormatFromHttpHeader(String httpHeader) throws DSLCoreException {
        switch (httpHeader) {
            case "application/json": return SerializationFormat.JSON;
            case "application/xml": return SerializationFormat.XML;
            case "application/x-yaml": return SerializationFormat.YAML;
        }

        throw new DSLCoreException("Unknown file http header:" + httpHeader);
    }

    public static String getHttpHeaderFromFormat(SerializationFormat format) throws DSLCoreException {
        switch (format) {
            case JSON: return "application/json";
            case XML: return "application/xml";
            case YAML: return "application/x-yaml";
        }

        throw new DSLCoreException("Unknown SerializationFormat:" + format);
    }


    public static SerializationFormat getFormatFromFileExtension(String fileExtension) throws DSLCoreException {
        switch (fileExtension) {
            case "json": return SerializationFormat.JSON;
            case "xml": return SerializationFormat.XML;
            case "yaml": return SerializationFormat.YAML;
            case "yml": return SerializationFormat.YAML;
        }

        throw new DSLCoreException("Unknown file extension:" + fileExtension);
    }

    public static String getFileExtensionFromFFrom(SerializationFormat format) throws  DSLCoreException {
        switch (format) {
            case JSON: return "json";
            case XML: return "xml";
            case YAML: return "yaml";
        }

        throw new DSLCoreException("Unknown SerializationFormat:" + format);
    }

}
