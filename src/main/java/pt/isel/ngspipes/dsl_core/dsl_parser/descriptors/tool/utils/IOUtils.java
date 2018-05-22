package pt.isel.ngspipes.dsl_core.dsl_parser.descriptors.tool.utils;

import utils.ToolRepositoryException;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

public class IOUtils {

    public static boolean existDirectory(String path) throws ToolRepositoryException {
        File file = new File(path);
        return file.exists() && file.isDirectory();
    }

    public static boolean existFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
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

    public static void copyFile(String sourcePath, String destPath) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File(sourcePath));
            os = new FileOutputStream(new File(destPath));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
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

    public static void createFolder(String dirPath) {
        File dirFile = new File(dirPath);
        if(dirFile == null || !dirFile.exists())
            dirFile.mkdirs();
    }


    public static void deleteFolder(String dirPath) {
        File dirFile = new File(dirPath);
        if(dirFile != null && dirFile.exists()) {
            for (File file : dirFile.listFiles()) {
                if(file.isDirectory())
                    deleteFolder(file.getAbsolutePath());
                file.delete();
            }
            dirFile.delete();
        }
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
}
