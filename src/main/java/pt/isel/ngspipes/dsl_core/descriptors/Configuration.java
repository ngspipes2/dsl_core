package pt.isel.ngspipes.dsl_core.descriptors;

import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;

public class Configuration {

    public static final Serialization.Format DEFAULT_TOOL_SERIALIZATION_FORMAT = Serialization.Format.JSON;
    public static final Serialization.Format DEFAULT_PIPELINE_SERIALIZATION_FORMAT = Serialization.Format.JSON;

    public static final String GITHUB_FILE_AND_DIRECTORY_NAMES_KEY = "name";
    public static final String GITHUB_BASE_URI = "https://github.com/";
    public static final String GITHUB_API_URI = "https://api.github.com/repos/";
    public static final String GITHUB_RAW_URI = "https://raw.githubusercontent.com/";

}
