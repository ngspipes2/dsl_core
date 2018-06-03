package pt.isel.ngspipes.dsl_core.descriptors.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import pt.isel.ngspipes.dsl_core.descriptors.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.LinkedList;

public class GithubAPI {

    public static boolean isGithubUri(String uri) {
        return uri.startsWith(Configuration.GITHUB_BASE_URI);
    }

    public static boolean existsRepository(String repositoryUri) throws IOException {
        String uri = getApiUri(repositoryUri, null);
        return HttpUtils.canConnect(uri);
    }


    public static String getFileContent(String repositoryUri, String path) throws IOException {
        String uri = getRawUri(repositoryUri, path);
        return HttpUtils.get(uri);
    }

    public static byte[] getFileBytes(String repositoryUri, String path) throws IOException {
        String uri = getRawUri(repositoryUri, path);
        return HttpUtils.getBytes(uri);
    }


    public static Collection<String> getFoldersNames(String repositoryUri) throws IOException {
        return getFoldersNames(repositoryUri, null);
    }

    public static Collection<String> getFoldersNames(String repositoryUri, String directory) throws IOException {
        return getNames(repositoryUri, directory, "dir");
    }

    public static Collection<String> getFilesNames(String repositoryUri) throws IOException {
        return getFilesNames(repositoryUri, null);
    }

    public static Collection<String> getFilesNames(String repositoryUri, String directory) throws IOException {
        return getNames(repositoryUri, directory, "file");
    }


    private static Collection<String> getNames(String repositoryUri, String directory, String type) throws IOException {
        String uri = getApiUri(repositoryUri, directory);

        JsonNode jsonNode = getJsonNodeFrom(uri);

        Collection<String> names = new LinkedList<>();

        if(jsonNode == null)
            return names;

        if(jsonNode.isArray()) {
            String name;
            for (JsonNode jNode : jsonNode) {
                if(jNode.get("type").textValue().equals(type)) {
                    name = jNode.get(Configuration.GITHUB_FILE_AND_DIRECTORY_NAMES_KEY).textValue();
                    names.add(name);
                }
            }
        }

        return names;
    }

    private static JsonNode getJsonNodeFrom(String uri) throws IOException {
        HttpURLConnection connection = HttpUtils.getGETConnection(uri);

        try {
            String content = IOUtils.toString(connection.getInputStream());
            return new ObjectMapper().readTree(content);
        } catch (IOException e) {
            int statusCode = connection.getResponseCode();

            if(statusCode == HttpStatus.SC_NO_CONTENT)
                return null;

            String errorMessage = IOUtils.toString(connection.getErrorStream());
            if(statusCode == HttpStatus.SC_NOT_FOUND && errorMessage.contains("empty"))
                return null;

            throw e;
        }
    }

    private static String getApiUri(String repositoryUri, String directory) {
        String uri = repositoryUri.replace(Configuration.GITHUB_BASE_URI, Configuration.GITHUB_API_URI);

        uri += "/contents";

        if(directory != null)
            uri += "/" + directory;

        return uri;
    }

    private static String getRawUri(String repositoryUri, String path) {
        String uri = repositoryUri.replace(Configuration.GITHUB_BASE_URI, Configuration.GITHUB_RAW_URI);

        return uri + "/master/" + path;
    }

}
