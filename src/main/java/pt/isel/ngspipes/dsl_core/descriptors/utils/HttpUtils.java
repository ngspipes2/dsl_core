package pt.isel.ngspipes.dsl_core.descriptors.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static HttpURLConnection getConnection(String url) throws IOException {
        return (HttpURLConnection) new URL(url).openConnection();
    }

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

}
