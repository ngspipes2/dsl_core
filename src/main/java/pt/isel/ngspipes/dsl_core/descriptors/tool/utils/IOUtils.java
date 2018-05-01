package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ToolRepositoryException;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

public class IOUtils {

    public static SupportedRepository getRepositoriesSupportedData(String nodeName) throws ToolRepositoryException {
        URL path = ClassLoader.getSystemClassLoader().getResource("./supported_repositories_types.json");
        JsonParser jsonParser = null;
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
            throw new ToolRepositoryException("Couldn't getAssociatedSupportedRepositoryInfo repository", e);
        }
        return null;
    }


    public static Collection<String> getDirectoryFilesName(String path) {
        Collection<String> names = new LinkedList<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file: listOfFiles) {
            if (file.isFile()) {
                names.add(file.getName());
            }
        }
         return names;
    }

    public static Collection<String> getSubDirectoriesName(String path) {
        Collection<String> names = new LinkedList<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file: listOfFiles) {
            if (file.isDirectory()) {
                names.add(file.getName());
            }
        }
        return names;
    }

    public static String getContent(String path) throws IOException {
        if(new File(path).exists()) {
            return readContent(path);
        }
        return null;
    }

    public static void write(String info, String fileName) throws IOException {
        PrintWriter writer = null;

        try{
            writer  = new PrintWriter(new File(fileName));

            writer.println(info);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static String read(String filePath) throws IOException {
        if(!new File(filePath).exists())
            filePath = getAbsolutePath(filePath);
        return readContent(filePath);
    }

    private static String readContent(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String str;
        try {
            br = new BufferedReader(new FileReader(filePath));
            while((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
        } finally {
            if (br != null)
                br.close();
        }

        return sb.toString();
    }

    private static String getAbsolutePath(String fileName){
        URL s = ClassLoader.getSystemResource(fileName);
        return s.getPath().substring(1);
    }

    public static void copyDirectory(File src, File dst) throws IOException{
        if (src.isDirectory()) {
            if (!dst.exists())
                dst.mkdir();

            for(String children : src.list())
                copyDirectory(new File(src, children), new File(dst, children));

        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);

            in.close();
            out.close();
        }

    }

    public static String getExtensionFromFilePath(String path) {
        StringBuilder extension = new StringBuilder(path);
        int lastPointIdx = path.lastIndexOf('.');
        return extension.substring(lastPointIdx + 1);
    }
}
