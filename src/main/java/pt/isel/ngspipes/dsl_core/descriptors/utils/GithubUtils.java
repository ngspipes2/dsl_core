package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class GithubUtils {

    public static JsonNode getJsonNodeFrom(String uri) throws IOException {
        String content = HttpUtils.get(uri);
        return new ObjectMapper().readTree(content);
    }

    public static JsonNode getJsonField(String uri, String propertyName) throws IOException {
        JsonNode jsonNode = getJsonNodeFrom(uri);
        if(propertyName != null || !propertyName.isEmpty())
            return jsonNode.get(propertyName);
        return jsonNode;
    }

    public static Collection<String> getFoldersNames(String uri, String propertyName) throws IOException {
        return getJSONPropertyValueByName(uri, propertyName, "dir");
    }

    public static Collection<String> getFilesNames(String uri, String propertyName) throws IOException {
        return getJSONPropertyValueByName(uri, propertyName, "file");
    }

    private static Collection<String> getJSONPropertyValueByName(String uri, String propertyName, String type) throws IOException {
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
    }

}
