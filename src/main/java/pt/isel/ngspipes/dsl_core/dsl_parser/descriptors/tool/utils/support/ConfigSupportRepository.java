package pt.isel.ngspipes.dsl_core.dsl_parser.descriptors.tool.utils.support;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ToolRepositoryException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ConfigSupportRepository {

    private static final String GITHUB_REPO_SUPPORT_LABEL = "Github";

    public static String github_base_uri;
    public static String github_access_uri;
    public static String github_api_uri;

    static {
        try {
            loadSupportedRepositoryInfo();
        } catch (ToolRepositoryException e) {
            throw new RuntimeException("Error loading Github repository configurations", e);
        }
    }

    public static void loadSupportedRepositoryInfo() throws ToolRepositoryException {
        loadGithubSupport();
    }



    private static void loadGithubSupport() throws ToolRepositoryException {
        SupportedRepository github = getRepositoriesSupportedData(GITHUB_REPO_SUPPORT_LABEL);
        if(github != null) {
            github_base_uri = github.base_uri;
            github_access_uri = github.access_uri;
            github_api_uri = github.api_uri;
        }
    }

    private static SupportedRepository getRepositoriesSupportedData(String nodeName) throws ToolRepositoryException {
        URL path = ClassLoader.getSystemClassLoader().getResource("./supported_repositories_types.json");
        JsonParser jsonParser;
        try {
            jsonParser = new JsonFactory().createParser(new File(path.getPath()));
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            MappingIterator<JsonNode> rootNode = mapper.readValues(jsonParser, JsonNode.class);

            if(rootNode.hasNext()){
                for (JsonNode node : rootNode.next().findValues(nodeName)) {
                    jsonParser = new JsonFactory().createParser(node.toString());

                    MappingIterator<SupportedRepository> supportedRepositories = mapper.readValues(jsonParser, SupportedRepository.class);
                    return supportedRepositories.hasNext() ? supportedRepositories.next() : null;
                }
            }
        } catch (IOException e) {
            throw new ToolRepositoryException("Couldn't loadSupportedRepositoryInfo repository", e);
        }
        return null;
    }
}
