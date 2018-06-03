package pt.isel.ngspipes.dsl_core.descriptors.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    public static boolean canConnect(String location) throws IOException {
        HttpURLConnection connection = getConnection(location);
        int responseCode = connection.getResponseCode();

        return responseCode >= 200 && responseCode < 400;
    }


    public static String get(String uri) throws IOException {
        HttpURLConnection connection = getConnection(uri);
        return IOUtils.toString(connection.getInputStream());
    }

    public static byte[] getBytes(String uri) throws IOException {
        HttpURLConnection connection = getConnection(uri);
        return IOUtils.toByteArray(connection.getInputStream());
    }


    public static HttpURLConnection getGETConnection(String url) throws IOException {
        return getGETConnection(url, new HashMap<>());
    }

    public static HttpURLConnection getGETConnection(String url, Map<String, String> headers) throws IOException {
        return getConnection(url, "GET", headers, true, false);
    }

    public static HttpURLConnection getPOSTConnection(String url) throws IOException {
        return getPOSTConnection(url, new HashMap<>());
    }

    public static HttpURLConnection getPOSTConnection(String url, Map<String, String> headers) throws IOException {
        return getConnection(url, "POST", headers, true, true);
    }

    public static HttpURLConnection getPUTConnection(String url) throws IOException {
        return getPUTConnection(url, new HashMap<>());
    }

    public static HttpURLConnection getPUTConnection(String url, Map<String, String> headers) throws IOException {
        return getConnection(url, "PUT", headers, true, true);
    }

    public static HttpURLConnection getDELETEConnection(String url) throws IOException {
        return getDELETEConnection(url, new HashMap<>());
    }

    public static HttpURLConnection getDELETEConnection(String url, Map<String, String> headers) throws IOException {
        return getConnection(url, "DELETE", headers, true, true);
    }

    private static HttpURLConnection getConnection(String url, String method, Map<String, String> headers,
                                                   boolean doInput, boolean doOutput) throws IOException {
        HttpURLConnection connection = getConnection(url);

        for(String header : headers.keySet())
            connection.setRequestProperty(header, headers.get(header));

        connection.setDoInput(doInput);
        connection.setDoOutput(doOutput);

        connection.setRequestMethod(method);

        return connection;
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

}
