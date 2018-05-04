package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import utils.ToolRepositoryException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
