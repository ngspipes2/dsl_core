package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ToolRepositoryException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.LinkedList;

public class HttpUtils {

    protected static HttpURLConnection getConnection(String url)throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

    public static boolean canConnect(String location) {
        try {
            HttpURLConnection connection = getConnection(location);
            int responseCode = connection.getResponseCode();
            return (responseCode >= 200 && responseCode < 400);
        } catch (IOException e) {
            throw new ToolRepositoryException("Not supported to connect to location: " + location, e);
        }
    }

    public static String getContent(String uri) throws ToolRepositoryException {
        try {
            HttpURLConnection connection = getConnection(uri);
            return readStream(connection);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading content", e);
        }
    }

    public static JsonNode getJsonNodeFrom(String uri) throws ToolRepositoryException {
        try {
            String content = getContent(uri);
            return new ObjectMapper().readTree(content);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading content", e);
        }
    }

    public static JsonNode getJsonFieldFromNode(String uri, String fieldname) throws ToolRepositoryException {
        JsonNode jsonNode = getJsonNodeFrom(uri);
        if(fieldname != null || !fieldname.isEmpty())
            return jsonNode.get(fieldname);
        return jsonNode;
    }

    public static Collection<String> getJsonFieldsValuesFromArray(String uri, String fieldName) throws ToolRepositoryException {
        try {
            Collection<String> fieldValues = new LinkedList<>();
            HttpURLConnection connection = getConnection(uri);
            String content = readStream(connection);
            JsonNode jsonNodeArr = new ObjectMapper().readTree(content);
            if(jsonNodeArr.isArray()) {
                for (JsonNode jsonNode : jsonNodeArr) {
                    String currValue = jsonNode.get(fieldName).textValue();
                    fieldValues.add(currValue);
                }
            }
            return fieldValues;
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading content", e);
        }
    }


    public static String readStream(URLConnection conn) throws IOException{
        BufferedReader br = null;
        String line;
        StringBuilder sb = new StringBuilder();

        try{
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = br.readLine()) != null)
                sb.append(line);

        } finally {
            if(br!=null)
                br.close();
        }

        return sb.toString();
    }
}
