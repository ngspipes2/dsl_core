package pt.isel.ngspipes.dsl_core.descriptors.utils;

import org.apache.commons.io.IOUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

public class GithubAPI {

    public static GitHub getGitHub(String userName, String password, String email, String accessToken) throws IOException {
        if(accessToken != null)
            return GitHub.connectUsingOAuth(accessToken);

        if(password != null)
            return GitHub.connectUsingPassword(email != null ? email : userName, password);

        return GitHub.connectAnonymously();
    }

    public static GHRepository getGHRepository(GitHub github, String repositoryName) throws IOException {
        if(existsRepository(github, repositoryName))
            return github.getRepository(repositoryName);
        else
            return null;
    }


    public static boolean existsRepository(GitHub github, String repositoryName) throws IOException {
        try {
            if(!repositoryName.contains("/"))
                return false;

            github.getRepository(repositoryName);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }


    public static String getFileContent(GHRepository repository, String path) throws IOException {
        GHContent file = repository.getFileContent(path);
        return IOUtils.toString(file.read());
    }

    public static byte[] getFileBytes(GHRepository repository, String path) throws IOException {
        GHContent file = repository.getFileContent(path);
        return IOUtils.toByteArray(file.read());
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

}
