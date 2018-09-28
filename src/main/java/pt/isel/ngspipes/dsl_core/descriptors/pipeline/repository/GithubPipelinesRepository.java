package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.fileBased.FileBasedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.JacksonEntityService;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelineSerialization;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils.PipelinesDescriptorUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.GithubAPI;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GithubPipelinesRepository extends WrapperPipelinesRepository {

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



    private class PipelineInfo {

        public String pipelineName;
        public String pipelineDirectory;
        public boolean existsPipeline;

        public String pipelineDescriptorName;
        public String pipelineDescriptorDirectory;
        public String pipelineDescriptorPath;
        public boolean existsPipelineDescriptor;

        public String pipelineLogoName;
        public String pipelineLogoDirectory;
        public String pipelineLogoPath;
        public boolean existsPipelineLogo;

        public PipelineSerialization.Format serializationFormat;



        public PipelineInfo( ) { }

        public PipelineInfo(    String pipelineName, String pipelineDirectory, boolean existsPipeline,
                                String pipelineDescriptorName, String pipelineDescriptorDirectory, String pipelineDescriptorPath, boolean existsPipelineDescriptor,
                                String pipelineLogoName, String pipelineLogoDirectory, String pipelineLogoPath, boolean existsPipelineLogo,
                                PipelineSerialization.Format serializationFormat) {
            this.pipelineName = pipelineName;
            this.pipelineDirectory = pipelineDirectory;
            this.existsPipeline = existsPipeline;

            this.pipelineDescriptorName = pipelineDescriptorName;
            this.pipelineDescriptorDirectory = pipelineDescriptorDirectory;
            this.pipelineDescriptorPath = pipelineDescriptorPath;
            this.existsPipelineDescriptor = existsPipelineDescriptor;

            this.pipelineLogoName = pipelineLogoName;
            this.pipelineLogoDirectory = pipelineLogoDirectory;
            this.pipelineLogoPath = pipelineLogoPath;
            this.existsPipelineLogo = existsPipelineLogo;

            this.serializationFormat = serializationFormat;
        }

    }



    private String userName;
    private String password;
    private String email;
    private String token;
    private PipelineSerialization.Format serializationFormat;
    private GitHub github;
    private GHRepository repository;



    public GithubPipelinesRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_PIPELINE_SERIALIZATION_FORMAT);
    }

    public GithubPipelinesRepository(String location, Map<String, Object> config, PipelineSerialization.Format serializationFormat) {
        super(location, config);

        this.userName = (String)super.config.get(USER_NAME_CONFIG_KEY);
        this.password = (String)super.config.get(PASSWORD_CONFIG_KEY);
        this.email = (String)super.config.get(EMAIL_CONFIG_KEY);
        this.token = (String)super.config.get(ACCESS_TOKEN_CONFIG_KEY);
        this.serializationFormat = serializationFormat;
    }



    // IMPLEMENTATION OF PipelinesRepository
    @Override
    public Collection<IPipelineDescriptor> getAllWrapped() throws PipelinesRepositoryException {
        init();

        Collection<String> names = getPipelinesName();
        Collection<IPipelineDescriptor> pipelines = new LinkedList<>();

        for (String name: names)
            pipelines.add(get(name));

        return pipelines;
    }


    @Override
    public IPipelineDescriptor getWrapped(String pipelineName) throws PipelinesRepositoryException {
        init();

        PipelineInfo pipelineInfo = createPipelineInfo(pipelineName);

        if(!pipelineInfo.existsPipeline)
            return null;

        return get(pipelineInfo);
    }

    private IPipelineDescriptor get(PipelineInfo info) throws PipelinesRepositoryException {
        try {
            IPipelineDescriptor pipelineDescriptor = getPipelineDescriptor(info);
            pipelineDescriptor.setLogo(getPipelineLogo(info));

            return pipelineDescriptor;
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error loading " + info.pipelineLogoName + " pipeline descriptor!", e);
        }
    }

    private IPipelineDescriptor getPipelineDescriptor(PipelineInfo info) throws IOException, PipelinesRepositoryException {
        String content = GithubAPI.getFileContent(repository, info.pipelineDescriptorPath);

        IPipelineDescriptor pipelineDescriptor = PipelinesDescriptorUtils.createPipelineDescriptor(content, info.serializationFormat);
        pipelineDescriptor.setName(info.pipelineName);

        return pipelineDescriptor;
    }

    private byte[] getPipelineLogo(PipelineInfo info) throws IOException {
        if(!info.existsPipelineLogo)
            return null;

        return GithubAPI.getFileBytes(repository, info.pipelineLogoPath);
    }


    @Override
    public void updateWrapped(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        init();

        PipelineInfo pipelineInfo = createPipelineInfo(pipeline.getName());

        if(!pipelineInfo.existsPipeline)
            throw new PipelinesRepositoryException("There is no pipeline with name: " + pipeline.getName());

        update(pipelineInfo, pipeline);
    }

    private void update(PipelineInfo info, IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        try {
            updatePipelineDescriptor(info, pipeline);
            updatePipelineLogo(info, pipeline);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error writing " + pipeline.getName() + " pipeline descriptor", e);
        }
    }

    private void updatePipelineDescriptor(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException, PipelinesRepositoryException {
        FileBasedPipelineDescriptor fileBasedPipeline = JacksonEntityService.transformToFileBasedPipelineDescriptor(pipeline);

        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(fileBasedPipeline, info.serializationFormat);

        GithubAPI.updateFile(repository, info.pipelineDescriptorPath, content);
    }

    private void updatePipelineLogo(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException {
        if(pipeline.getLogo() != null) {
            if(info.existsPipelineLogo)
                GithubAPI.updateFile(repository, info.pipelineLogoPath, pipeline.getLogo());
            else
                GithubAPI.createFile(repository, info.pipelineLogoPath, pipeline.getLogo());
        } else {
            if(info.existsPipelineLogo)
                GithubAPI.deleteFile(repository, info.pipelineLogoDirectory, info.pipelineDescriptorName);
        }
    }


    @Override
    public void insertWrapped(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        init();

        PipelineInfo pipelineInfo = createPipelineInfo(pipeline.getName());

        if(pipelineInfo.existsPipeline)
            throw new PipelinesRepositoryException("There is already a pipeline with name: " + pipeline.getName());

       insert(pipelineInfo, pipeline);
    }

    private void insert(PipelineInfo info, IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        try {
            insertPipelineDescriptor(info, pipeline);
            insertPipelineLogo(info, pipeline);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Error writing " + pipeline.getName() + " pipeline descriptor", e);
        }
    }

    private void insertPipelineDescriptor(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException, PipelinesRepositoryException {
        FileBasedPipelineDescriptor fileBasedPipeline = JacksonEntityService.transformToFileBasedPipelineDescriptor(pipeline);

        String content = PipelinesDescriptorUtils.getPipelineDescriptorAsString(fileBasedPipeline, info.serializationFormat);

        GithubAPI.createFile(repository, info.pipelineDescriptorPath, content);
    }

    private void insertPipelineLogo(PipelineInfo info, IPipelineDescriptor pipeline) throws IOException {
        if(pipeline.getLogo() != null)
            GithubAPI.createFile(repository, info.pipelineLogoPath, pipeline.getLogo());
    }


    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        init();

        PipelineInfo pipelineInfo = createPipelineInfo(pipelineName);

        if(!pipelineInfo.existsPipeline)
            return;

        delete(pipelineInfo);
    }

    private void delete(PipelineInfo info) throws PipelinesRepositoryException {
        try {
            deletePipelineDescriptor(info);
            deletePipelineLogo(info);
        } catch (IOException e) {
            throw new PipelinesRepositoryException("Could not delete folder " + info.pipelineDirectory+"!", e);
        }
    }

    private void deletePipelineDescriptor(PipelineInfo info) throws IOException {
        GithubAPI.deleteFile(repository, info.pipelineDescriptorDirectory, info.pipelineDescriptorName);
    }

    private void deletePipelineLogo(PipelineInfo info) throws IOException {
        if(info.existsPipelineLogo)
            GithubAPI.deleteFile(repository, info.pipelineLogoDirectory, info.pipelineLogoName);
    }


    private void init() throws PipelinesRepositoryException {
        if(github == null) {
            try {
                this.github = GithubAPI.getGitHub(userName, password, email, token);
            } catch (IOException e) {
                throw new PipelinesRepositoryException("Error getting GitHub!", e);
            }
        }

        if(repository == null) {
            try {
                this.repository = GithubAPI.getGHRepository(github, super.location);
            } catch (IOException e) {
                throw new PipelinesRepositoryException("Error getting GHRepository!", e);
            }
        }
    }

    private PipelineInfo createPipelineInfo(String pipelineName) throws PipelinesRepositoryException {
        try {
            PipelineInfo info = new PipelineInfo();

            info.pipelineName = pipelineName;
            info.pipelineDirectory = pipelineName;
            info.existsPipeline = GithubAPI.existsFolder(repository, info.pipelineDirectory);

            info.pipelineDescriptorName = getDescriptorName(info.pipelineDirectory);
            info.pipelineDescriptorDirectory = info.pipelineDirectory;
            info.pipelineDescriptorPath = info.pipelineDescriptorDirectory + SEPARATOR + info.pipelineDescriptorName;
            info.existsPipelineDescriptor = GithubAPI.existsFile(repository, info.pipelineDescriptorDirectory, info.pipelineDescriptorName);

            info.pipelineLogoName = LOGO_FILE_NAME;
            info.pipelineLogoDirectory = info.pipelineDirectory;
            info.pipelineLogoPath = info.pipelineLogoDirectory + SEPARATOR + LOGO_FILE_NAME;
            info.existsPipelineLogo = GithubAPI.existsFile(repository, info.pipelineLogoDirectory, LOGO_FILE_NAME);

            info.serializationFormat = PipelineSerialization.getFormatFromFileExtension(IOUtils.getExtensionFromFilePath(info.pipelineDescriptorPath));

            return info;
        } catch (DSLCoreException | IOException e) {
            throw new PipelinesRepositoryException("Error checking info for pipeline " + pipelineName + "!", e);
        }
    }

    private String getDescriptorName(String pipelineDirectory) throws IOException, DSLCoreException {
        for(String file : GithubAPI.getFilesNames(repository, pipelineDirectory)) {
            if(file.startsWith(DESCRIPTOR_FILE_NAME))
                return file;
        }

        String extension = "." + PipelineSerialization.getFileExtensionFromFormat(serializationFormat);
        return DESCRIPTOR_FILE_NAME + extension;
    }

    private Collection<String> getPipelinesName() throws PipelinesRepositoryException {
        try {
            return GithubAPI.getFoldersNames(repository);
        } catch(IOException e) {
            throw new PipelinesRepositoryException("Error getting pipelines names!", e);
        }
    }

}
