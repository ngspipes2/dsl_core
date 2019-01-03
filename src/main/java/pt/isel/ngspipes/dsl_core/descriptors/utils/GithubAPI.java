package pt.isel.ngspipes.dsl_core.descriptors.utils;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class GithubAPI {

    public static GitHub getGitHub(String userName, String password, String email, String accessToken) throws IOException {
        if(accessToken != null)
            return GitHub.connectUsingOAuth(accessToken);

        if(password != null)
            return GitHub.connectUsingPassword(email != null ? email : userName, password);

        return GitHub.connectAnonymously();
    }

    public static GHRepository getGHRepository(GitHub github, String repositoryUri) throws IOException {
        String[] tokens = repositoryUri.split("/");
        String repositoryName = tokens[tokens.length-2] + "/" + tokens[tokens.length-1];

        if(existsRepository(github, repositoryUri))
            return github.getRepository(repositoryName);
        else
            return null;
    }


    public static boolean existsRepository(GitHub github, String repositoryUri) throws IOException {
        try {
            if(!repositoryUri.contains("/") || !repositoryUri.startsWith("https://github.com"))
                return false;

            String[] tokens = repositoryUri.split("/");
            String repositoryName = tokens[tokens.length-2] + "/" + tokens[tokens.length-1];

            github.getRepository(repositoryName);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static boolean existsFile(GHRepository repository, String name) throws IOException {
        return existsFile(repository,"", name);
    }

    public static boolean existsFile(GHRepository repository, String path, String name) throws IOException {
        if(!existsFolder(repository, path))
            return false;

        try {
            return repository.getDirectoryContent(path)
                    .stream()
                    .anyMatch(content -> content.isFile() && content.getName().equals(name));
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    public static boolean existsFolder(GHRepository repository, String name) throws IOException {
        return existsFolder(repository, "", name);
    }

    public static boolean existsFolder(GHRepository repository, String path, String name) throws IOException {
        if(path.equals("") && name.equals(""))
            return true;

        if(path.equals("/") && name.equals(""))
            return true;

        try {
            return repository.getDirectoryContent(path)
                .stream()
                .anyMatch(content -> content.isDirectory() && content.getName().equals(name));
        } catch (FileNotFoundException e) {
            return false;
        }
    }


    public static String getFileContent(GHRepository repository, String path) throws IOException {
        GHContent file = repository.getFileContent(path);

        //Forced to used deprecated "getContent()" because "read()" uses RAW_URL from github api which returns old version of file
        String content = file.getContent();

        return content;
    }

    public static byte[] getFileBytes(GHRepository repository, String path) throws IOException {
        GHContent file = repository.getFileContent(path);

        //Forced to used deprecated "getContent()" because "read()" uses RAW_URL from github api which returns old version of file
        String content = file.getContent();

        return content.getBytes();
    }


    public static Collection<String> getFoldersNames(GHRepository repository) throws IOException {
        return getFoldersNames(repository, "");
    }

    public static Collection<String> getFoldersNames(GHRepository repository, String directory) throws IOException {
        return getNames(repository, directory, false);
    }

    public static Collection<String> getFilesNames(GHRepository repository) throws IOException {
        return getFilesNames(repository, "");
    }

    public static Collection<String> getFilesNames(GHRepository repository, String directory) throws IOException {
        return getNames(repository, directory, true);
    }

    private static Collection<String> getNames(GHRepository repository, String directory, boolean fileNotDirectory) throws IOException {
        Collection<String> names = new LinkedList<>();

        try {
            for(GHContent content : repository.getDirectoryContent(directory))
                if(fileNotDirectory && content.isFile())
                    names.add(content.getName());
                else if(!fileNotDirectory && content.isDirectory())
                    names.add(content.getName());
        } catch (FileNotFoundException e) {
            return new LinkedList<>();
        }

        return names;
    }


    public static boolean deleteFile(GHRepository repository, String path, String name) throws IOException {
        List<GHContent> contents = repository.getDirectoryContent(path);

        Optional<GHContent> contentToDelete = contents
                .stream()
                .filter(content -> content.isFile() && content.getName().equals(name))
                .findFirst();

        if(contentToDelete.isPresent()) {
            contentToDelete.get().delete("Deleted " + name);
            return true;
        }

        return false;
    }


    public static void createFile(GHRepository repository, String path, String content) throws IOException {
        repository.createContent(content, "Creating file " + path, path);
    }

    public static void createFile(GHRepository repository, String path, byte[] content) throws IOException {
        repository.createContent(content, "Creating file " + path, path);
    }


    public static void updateFile(GHRepository repository, String path, String content) throws IOException {
        GHContent file = repository.getFileContent(path);
        file.update(content, "Updating file " + file);
    }

    public static void updateFile(GHRepository repository, String path, byte[] content) throws IOException {
        GHContent file = repository.getFileContent(path);
        file.update(content, "Updating file " + file);
    }

}
