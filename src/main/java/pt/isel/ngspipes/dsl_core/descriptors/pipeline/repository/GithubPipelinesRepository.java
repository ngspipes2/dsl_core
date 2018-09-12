package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelinesDescriptorUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.GithubAPI;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GithubPipelinesRepository extends PipelinesRepository {

    public static final String USER_NAME_CONFIG_KEY = "username";
    public static final String PASSWORD_CONFIG_KEY = "password";
    public static final String EMAIL_CONFIG_KEY = "email";
    public static final String ACCESS_TOKEN_CONFIG_KEY = "token";

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME = "Descriptor";
    private static final String SEPARATOR = "/";



    // IMPLEMENTATION OF IPipelinesRepositoryFactory
    public static IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        if(config == null)
            config = new HashMap<>();

        if(!verifyLocation(location, config))
            return null;

        return new GithubPipelinesRepository(location, config);
    }

    private static boolean verifyLocation(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        try {
            return GithubAPI.existsRepository(
                    GithubAPI.getGitHub(
                        (String)config.get(USER_NAME_CONFIG_KEY),
                        (String)config.get(PASSWORD_CONFIG_KEY),
                        (String)config.get(EMAIL_CONFIG_KEY),
                        (String)config.get(ACCESS_TOKEN_CONFIG_KEY)
                    ),
                    location);
        } catch (IOException e) {
           throw new PipelinesRepositoryException("Could not verify location:" + location, e);
        }
    }



    private String userName;
    private String password;
    private String email;
    private String token;



    public GithubPipelinesRepository(String location, Map<String, Object> config) {
        super(location, config);

        this.userName = (String)super.config.get(USER_NAME_CONFIG_KEY);
        this.password = (String)super.config.get(PASSWORD_CONFIG_KEY);
        this.email = (String)super.config.get(EMAIL_CONFIG_KEY);
        this.token = (String)super.config.get(ACCESS_TOKEN_CONFIG_KEY);
    }



    // IMPLEMENTATION OF PipelinesRepository
    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        Collection<String> names = getPipelinesName();
        Collection<IPipelineDescriptor> pipelines = new LinkedList<>();

        for (String name: names)
            pipelines.add(get(name));

        return pipelines;
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        try {
            if(!getPipelinesName().contains(pipelineName))
                return null;

            String descriptorName = getDescriptorName(pipelineName);
            String descriptorPath = pipelineName + SEPARATOR + descriptorName;
            String content = GithubAPI.getFileContent(getGHRepository(), descriptorPath);
            String type = IOUtils.getExtensionFromFilePath(descriptorName);

            return createPipelineDescriptor(pipelineName, type, content);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error loading " + pipelineName + " pipeline descriptor", e);
        } catch (DSLCoreException e) {
            throw new PipelinesRepositoryException(e.getMessage(), e);
        }
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        throw new PipelinesRepositoryException("Update operation not supported");
    }

    @Override
    public void insert(IPipelineDescriptor pipelineName) throws PipelinesRepositoryException {
        throw new PipelinesRepositoryException("Insert operation not supported");
    }

    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        throw new PipelinesRepositoryException("Delete operation not supported");
    }


    private GitHub getGitHub() throws PipelinesRepositoryException {
        try {
            return GithubAPI.getGitHub(userName, password, email, token);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error getting GitHub!", e);
        }
    }

    private GHRepository getGHRepository() throws PipelinesRepositoryException {
        try {
            return GithubAPI.getGHRepository(getGitHub(), super.location);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error getting GHRepository!", e);
        }
    }

    private Collection<String> getPipelinesName() throws PipelinesRepositoryException {
        try {
            return GithubAPI.getFoldersNames(getGHRepository());
        } catch(IOException e) {
            throw new PipelinesRepositoryException("Error getting pipelines names!", e);
        }
    }

    private IPipelineDescriptor createPipelineDescriptor(String pipelineName, String type, String content) throws IOException, PipelinesRepositoryException, DSLCoreException {
        PipelineSerialization.Format format = PipelineSerialization.getFormatFromFileExtension(type);

        IPipelineDescriptor pipelineDescriptor = PipelinesDescriptorUtils.createPipelineDescriptor(content, format);

        pipelineDescriptor.setName(pipelineName);
        pipelineDescriptor.setLogo(getLogo(pipelineName));

        return pipelineDescriptor;
    }

    private String getDescriptorName(String pipelineName) throws IOException, PipelinesRepositoryException {
        GHRepository repository = getGHRepository();
        String descriptorName;
        Collection<String> names = GithubAPI.getFilesNames(repository, pipelineName);

        for (String currName: names) {
            String type = IOUtils.getExtensionFromFilePath(currName);
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("." + type));

            if(currName.equals(DESCRIPTOR_FILE_NAME))
                return descriptorName;
        }

        throw new PipelinesRepositoryException("Couldn't find descriptor for pipeline " + pipelineName);
    }

    private byte[] getLogo(String pipelineName) throws IOException, PipelinesRepositoryException {
        GHRepository repository = getGHRepository();
        Collection<String> files = GithubAPI.getFilesNames(repository, pipelineName);

        if(files.contains(LOGO_FILE_NAME))
            return GithubAPI.getFileBytes(repository, pipelineName + SEPARATOR + LOGO_FILE_NAME);

        return null;
    }

}
