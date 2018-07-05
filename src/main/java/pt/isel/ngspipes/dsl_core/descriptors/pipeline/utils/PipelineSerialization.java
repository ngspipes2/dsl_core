package pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils;

import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

public class PipelineSerialization {

    public enum Format {
        PIPES,
        JSON,
        YAML
    }



    public static Serialization.Format getFormatFromHttpHeader(String httpHeader) throws DSLCoreException {
        if(httpHeader.contains("application/json"))
            return Serialization.Format.JSON;

        if(httpHeader.contains("application/x-yaml"))
            return Serialization.Format.YAML;

        throw new DSLCoreException("Unknown http header:" + httpHeader);
    }

    public static String getHttpHeaderFromFormat(Format format) throws DSLCoreException {
        switch (format) {
            case PIPES: throw new DSLCoreException("There is no header for Pipes format!");
            case JSON: return "application/json";
            case YAML: return "application/x-yaml";
        }

        throw new DSLCoreException("Unknown Format:" + format);
    }


    public static Format getFormatFromFileExtension(String fileExtension) throws DSLCoreException {
        switch (fileExtension) {
            case "pipes": return Format.PIPES;
            case "json": return Format.JSON;
            case "yaml": return Format.YAML;
            case "yml": return Format.YAML;
        }

        throw new DSLCoreException("Unknown file extension:" + fileExtension);
    }

    public static String getFileExtensionFromFormat(PipelineSerialization.Format format) throws  DSLCoreException {
        switch (format) {
            case PIPES: return "pipes";
            case JSON: return "json";
            case YAML: return "yaml";
        }

        throw new DSLCoreException("Unknown Format:" + format);
    }


    public static Serialization.Format pipelineSerializationFormatToSerializationFormat(Format format) throws PipelinesRepositoryException {
        if(format == null)
            return null;

        switch (format) {
            case JSON: return Serialization.Format.JSON;
            case YAML: return Serialization.Format.YAML;
        }

        throw new PipelinesRepositoryException("Could not convert PipelineSerialization.Format " + format + " to Serialization.Format!");
    }

    public static Format serializationFormatToPipelineSerializationFormat(Serialization.Format format) throws PipelinesRepositoryException {
        if(format == null)
            return null;

        switch (format) {
            case JSON: return Format.JSON;
            case YAML: return Format.YAML;
        }

        throw new PipelinesRepositoryException("Could not convert Serialization.Format " + format + " to PipelineSerialization.Format!");
    }

}
