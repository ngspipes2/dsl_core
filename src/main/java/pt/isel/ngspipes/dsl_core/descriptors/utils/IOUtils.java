package pt.isel.ngspipes.dsl_core.descriptors.utils;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class IOUtils {

    public static void write(String content, String filePath) throws IOException {
        try(PrintWriter writer = new PrintWriter(filePath, "UTF-8")){
            writer.println(content);
        }
    }

    public static void writeBytes(byte[] content, String filePath) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(filePath)){
            fos.write(content);
        }
    }


    public static String read(String filePath) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return org.apache.commons.io.IOUtils.toString(br);
        }
    }

    public static byte[] readBytes(String filePath) throws IOException {
        try(InputStream is = new FileInputStream(filePath)) {
            return org.apache.commons.io.IOUtils.toByteArray(is);
        }
    }


    public static boolean existDirectory(String path) {
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

        if(listOfFiles != null) {
            for (File file: listOfFiles) {
                if (file.isFile()) {
                    names.add(file.getName());
                }
            }
        }

         return names;
    }

    public static Collection<String> getSubDirectoriesName(String path) {
        Collection<String> names = new LinkedList<>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles != null) {
            for (File file: listOfFiles) {
                if (file.isDirectory()) {
                    names.add(file.getName());
                }
            }
        }

        return names;
    }


    public static String getExtensionFromFilePath(String path) {
        StringBuilder extension = new StringBuilder(path);
        int lastPointIdx = path.lastIndexOf('.');
        return extension.substring(lastPointIdx + 1);
    }


    public static void createFolder(String dirPath) {
        File dirFile = new File(dirPath);

        if(!dirFile.exists())
            dirFile.mkdirs();
    }

    public static void deleteFolder(String dirPath) {
        File dirFile = new File(dirPath);

        if(dirFile.exists()) {
            if(dirFile.listFiles ()!= null){
                for (File file : Objects.requireNonNull(dirFile.listFiles())) {
                    if(file.isDirectory())
                        deleteFolder(file.getAbsolutePath());
                    file.delete();
                }
            }

            dirFile.delete();
        }
    }

    public static void deleteFile(String dirPath) {
        File file = new File(dirPath);

        if(file.exists())
            file.delete();
    }

}
