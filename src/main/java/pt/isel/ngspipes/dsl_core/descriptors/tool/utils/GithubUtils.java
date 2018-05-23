package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pt.isel.ngspipes.dsl_core.descriptors.utils.HttpUtils;
import utils.ToolRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class GithubUtils {

    public static JsonNode getJsonNodeFrom(String uri) throws ToolRepositoryException {
        try {
            String content = HttpUtils.get(uri);
            return new ObjectMapper().readTree(content);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading content", e);
        }
    }

    public static JsonNode getJsonField(String uri, String propertyName) throws ToolRepositoryException {
        JsonNode jsonNode = getJsonNodeFrom(uri);
        if(propertyName != null || !propertyName.isEmpty())
            return jsonNode.get(propertyName);
        return jsonNode;
    }

    public static Collection<String> getFoldersNames(String uri, String propertyName) throws ToolRepositoryException {
        return getJSONPropertyValueByName(uri, propertyName, "dir");
    }

    public static Collection<String> getFilesNames(String uri, String propertyName) throws ToolRepositoryException {
        return getJSONPropertyValueByName(uri, propertyName, "file");
    }

    private static Collection<String> getJSONPropertyValueByName(String uri, String propertyName, String type) throws ToolRepositoryException {
        try {
            Collection<String> fieldValues = new LinkedList<>();
            String content = HttpUtils.get(uri);
            JsonNode jsonNodeArr = new ObjectMapper().readTree(content);
            if(jsonNodeArr.isArray()) {
                for (JsonNode jsonNode : jsonNodeArr) {
                    if(jsonNode.get("type").textValue().equals(type)) {
                        String currValue = jsonNode.get(propertyName).textValue();
                        fieldValues.add(currValue);
                    }
                }
            }
            return fieldValues;
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading content", e);
        }
    }
}
