package pt.isel.ngspipes.dsl_core.descriptors;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;

public class Configuration {

    public static final Serialization.Format DEFAULT_TOOL_SERIALIZATION_FORMAT = Serialization.Format.JSON;
    public static final PipelineSerialization.Format DEFAULT_PIPELINE_SERIALIZATION_FORMAT = PipelineSerialization.Format.PIPES;

}
