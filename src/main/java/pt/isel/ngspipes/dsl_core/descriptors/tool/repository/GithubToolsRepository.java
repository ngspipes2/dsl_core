package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.fileBased.FileBasedToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.FileBasedToolsDescriptorsUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.JacksonEntityService;
import pt.isel.ngspipes.dsl_core.descriptors.utils.GithubAPI;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class  GithubToolsRepository extends WrapperToolsRepository {

    public static final String USER_NAME_CONFIG_KEY = "username";
    public static final String PASSWORD_CONFIG_KEY = "password";
    public static final String EMAIL_CONFIG_KEY = "email";
    public static final String ACCESS_TOKEN_CONFIG_KEY = "token";

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String SEPARATOR = "/";
    private static final String EXECUTION_CONTEXTS_SUB_DIR = "execution_contexts";



    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        if(config == null)
           config = new HashMap<>();

        if(!verifyLocation(location, config))
            return null;

        return new GithubToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location, Map<String, Object> config) throws ToolsRepositoryException {
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
           throw new ToolsRepositoryException("Could not verify location:" + location, e);
        }
    }



    private class ToolInfo {

        public String toolName;
        public String toolDirectory;
        public boolean existsTool;

        public String toolDescriptorName;
        public String toolDescriptorDirectory;
        public String toolDescriptorPath;
        public boolean existsToolDescriptor;

        public String toolLogoName;
        public String toolLogoDirectory;
        public String toolLogoPath;
        public boolean existsToolLogo;

        public String executionContextsDirectory;
        public Collection<String> executionContextsNames;
        public Collection<String> executionContextsPath;

        public Serialization.Format serializationFormat;



        public ToolInfo( ) { }

        public ToolInfo(String toolName, String toolDirectory, boolean existsTool,
                        String toolDescriptorName, String toolDescriptorDirectory, String toolDescriptorPath, boolean existsToolDescriptor,
                        String toolLogoName, String toolLogoDirectory, String toolLogoPath, boolean existsToolLogo,
                        String executionContextsDirectory, Collection<String> executionContextsNames, Collection<String> executionContextsPath,
                        Serialization.Format serializationFormat) {
            this.toolName = toolName;
            this.toolDirectory = toolDirectory;
            this.existsTool = existsTool;

            this.toolDescriptorName = toolDescriptorName;
            this.toolDescriptorDirectory = toolDescriptorDirectory;
            this.toolDescriptorPath = toolDescriptorPath;
            this.existsToolDescriptor = existsToolDescriptor;

            this.toolLogoName = toolLogoName;
            this.toolLogoDirectory = toolLogoDirectory;
            this.toolLogoPath = toolLogoPath;
            this.existsToolLogo = existsToolLogo;

            this.executionContextsDirectory = executionContextsDirectory;
            this.executionContextsNames = executionContextsNames;
            this.executionContextsPath = executionContextsPath;

            this.serializationFormat = serializationFormat;
        }

    }



    private String userName;
    private String password;
    private String email;
    private String token;
    private Serialization.Format serializationFormat;
    private GitHub github;
    private GHRepository repository;



    public GithubToolsRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_TOOL_SERIALIZATION_FORMAT);
    }

    public GithubToolsRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);

        this.userName = (String)super.config.get(USER_NAME_CONFIG_KEY);
        this.password = (String)super.config.get(PASSWORD_CONFIG_KEY);
        this.email = (String)super.config.get(EMAIL_CONFIG_KEY);
        this.token = (String)super.config.get(ACCESS_TOKEN_CONFIG_KEY);
        this.serializationFormat = serializationFormat;
    }



    // IMPLEMENTATION OF ToolsRepository
    @Override
    protected Collection<IToolDescriptor> getAllWrapped() throws ToolsRepositoryException {
        init();

        Collection<String> names = getToolsName();
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }


    @Override
    protected IToolDescriptor getWrapped(String toolName) throws ToolsRepositoryException {
        init();

        ToolInfo toolInfo = createToolInfo(toolName);

        if(!toolInfo.existsTool)
            return null;

        return get(toolInfo);
    }

    private IToolDescriptor get(ToolInfo info) throws ToolsRepositoryException {
        try {
            IToolDescriptor toolDescriptor = getToolDescriptor(info);
            toolDescriptor.setLogo(getToolLogo(info));
            toolDescriptor.setExecutionContexts(getToolExecutionContexts(info));

            return toolDescriptor;
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error loading " + info.toolName + " tool descriptor", e);
        }
    }

    private IToolDescriptor getToolDescriptor(ToolInfo info) throws IOException, ToolsRepositoryException {
        String content = GithubAPI.getFileContent(repository, info.toolDescriptorPath);

        IToolDescriptor toolDescriptor = FileBasedToolsDescriptorsUtils.createToolDescriptor(content, info.serializationFormat);

        return toolDescriptor;
    }

    private byte[] getToolLogo(ToolInfo info) throws IOException, ToolsRepositoryException {
        if(!info.existsToolLogo)
            return null;

        return GithubAPI.getFileBytes(repository, info.toolLogoPath);
    }

    private Collection<IExecutionContextDescriptor> getToolExecutionContexts(ToolInfo info) throws IOException, ToolsRepositoryException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();

        for (String contextPath : info.executionContextsPath) {
            String type = IOUtils.getExtensionFromFilePath(contextPath);

            String content = GithubAPI.getFileContent(repository, contextPath);

            IExecutionContextDescriptor context = FileBasedToolsDescriptorsUtils.createExecutionContextDescriptor(content, type);

            contexts.add(context);
        }

        return contexts;
    }


    @Override
    protected void updateWrapped(IToolDescriptor tool) throws ToolsRepositoryException {
        init();

        ToolInfo toolInfo = createToolInfo(tool.getName());

        if(!toolInfo.existsTool)
            throw new ToolsRepositoryException("There is no tool with name: " + tool.getName());

        update(toolInfo, tool);
    }

    private void update(ToolInfo info, IToolDescriptor tool) throws ToolsRepositoryException {
        try {
            updateToolDescriptor(info, tool);
            updateToolLogo(info, tool);
            updateToolExecutionContexts(info, tool);
        } catch (DSLCoreException | IOException e) {
            throw new ToolsRepositoryException("Error writing " + info.toolName + " tool descriptor", e);
        }
    }

    private void updateToolDescriptor(ToolInfo info, IToolDescriptor tool) throws IOException, ToolsRepositoryException {
        FileBasedToolDescriptor fileBasedToolDescriptor = JacksonEntityService.transformToFileBasedToolDescriptor(tool);

        String content = FileBasedToolsDescriptorsUtils.getToolDescriptorAsString(fileBasedToolDescriptor, info.serializationFormat);

        GithubAPI.updateFile(repository, info.toolDescriptorPath, content);
    }

    private void updateToolLogo(ToolInfo info, IToolDescriptor tool) throws IOException {
        if(tool.getLogo() != null) {
            if(info.existsToolLogo)
                GithubAPI.updateFile(repository, info.toolLogoPath, tool.getLogo());
            else
                GithubAPI.createFile(repository, info.toolLogoPath, tool.getLogo());
        } else {
            if(info.existsToolLogo)
                GithubAPI.deleteFile(repository, info.toolLogoDirectory, info.toolDescriptorName);
        }
    }

    private void updateToolExecutionContexts(ToolInfo info, IToolDescriptor tool) throws IOException, DSLCoreException, ToolsRepositoryException {
        String extension = "." + Serialization.getFileExtensionFromFormat(info.serializationFormat);

        String content;
        String path;
        for(IExecutionContextDescriptor executionContext : tool.getExecutionContexts()) {
            content = FileBasedToolsDescriptorsUtils.getExecutionContextDescriptorAsString(executionContext, info.serializationFormat);
            path = info.executionContextsDirectory + SEPARATOR + executionContext.getName() + extension;

            if(info.executionContextsNames.stream().anyMatch(ec -> ec.startsWith(executionContext.getName()+".")))
                GithubAPI.updateFile(repository, path, content);
            else
                GithubAPI.createFile(repository, path, content);
        }

        for(String fileName : info.executionContextsNames) {
            if(tool.getExecutionContexts()
                .stream()
                .noneMatch(ec -> ec.getName().equals(fileName.split(".")[0])))
                GithubAPI.deleteFile(repository, info.executionContextsDirectory, fileName);
        }
    }


    @Override
    protected void insertWrapped(IToolDescriptor tool) throws ToolsRepositoryException {
        init();

        ToolInfo toolInfo = createToolInfo(tool.getName());

        if(toolInfo.existsTool)
            throw new ToolsRepositoryException("There is already a tool with name: " + tool.getName());

        insert(toolInfo, tool);
    }

    private void insert(ToolInfo info, IToolDescriptor tool) throws ToolsRepositoryException {
        try {
            insertToolDescriptor(info, tool);
            insertToolLogo(info, tool);
            insertToolExecutionContexts(info, tool);
        } catch (DSLCoreException | IOException e) {
            throw new ToolsRepositoryException("Error writing " + tool.getName() + " tool descriptor", e);
        }
    }

    private void insertToolDescriptor(ToolInfo info, IToolDescriptor tool) throws IOException, ToolsRepositoryException {
        FileBasedToolDescriptor fileBasedToolDescriptor = JacksonEntityService.transformToFileBasedToolDescriptor(tool);

        String content = FileBasedToolsDescriptorsUtils.getToolDescriptorAsString(fileBasedToolDescriptor, info.serializationFormat);

        GithubAPI.createFile(repository, info.toolDescriptorPath, content);
    }

    private void insertToolLogo(ToolInfo info, IToolDescriptor tool) throws IOException {
        if(tool.getLogo() != null)
            GithubAPI.createFile(repository, info.toolLogoPath, tool.getLogo());
    }

    private void insertToolExecutionContexts(ToolInfo info, IToolDescriptor tool) throws IOException, ToolsRepositoryException, DSLCoreException {
        String extension = "." + Serialization.getFileExtensionFromFormat(info.serializationFormat);

        String content;
        String path;
        for(IExecutionContextDescriptor executionContext : tool.getExecutionContexts()) {
            content = FileBasedToolsDescriptorsUtils.getExecutionContextDescriptorAsString(executionContext, info.serializationFormat);
            path = info.executionContextsDirectory + SEPARATOR + executionContext.getName() + extension;
            GithubAPI.createFile(repository, path, content);
        }
    }


    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
        init();

        ToolInfo toolInfo = createToolInfo(toolName);

        if(!toolInfo.existsTool)
            return;

        delete(toolInfo);
    }

    private void delete(ToolInfo info) throws ToolsRepositoryException {
        try {
            deleteToolDescriptor(info);
            deleteToolLogo(info);
            deleteToolExecutionContexts(info);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Could not delete folder " + info.toolDirectory + "!", e);
        }
    }

    private void deleteToolDescriptor(ToolInfo info) throws IOException {
        GithubAPI.deleteFile(repository, info.toolDescriptorDirectory, info.toolDescriptorName);
    }

    private void deleteToolLogo(ToolInfo info) throws IOException {
        if(info.existsToolLogo)
            GithubAPI.deleteFile(repository, info.toolLogoDirectory, info.toolLogoName);
    }

    private void deleteToolExecutionContexts(ToolInfo info) throws IOException {
        for(String executionContextName : info.executionContextsNames)
            GithubAPI.deleteFile(repository, info.executionContextsDirectory, executionContextName);
    }


    private void init() throws ToolsRepositoryException {
        if(github == null) {
            try {
                this.github = GithubAPI.getGitHub(userName, password, email, token);
            } catch (IOException e) {
                throw new ToolsRepositoryException("Error getting GitHub!", e);
            }
        }

        if(repository == null) {
            try {
                this.repository = GithubAPI.getGHRepository(github, super.location);
            } catch (IOException e) {
                throw new ToolsRepositoryException("Error getting GHRepository!", e);
            }
        }
    }

    private ToolInfo createToolInfo(String toolName) throws ToolsRepositoryException {
        try {
            ToolInfo info = new ToolInfo();

            info.toolName = toolName;
            info.toolDirectory = toolName;
            info.existsTool = GithubAPI.existsFolder(repository, info.toolDirectory);

            info.toolDescriptorName = getDescriptorName(info.toolDirectory);
            info.toolDescriptorDirectory = info.toolDirectory;
            info.toolDescriptorPath = info.toolDescriptorDirectory + SEPARATOR + info.toolDescriptorName;
            info.existsToolDescriptor = GithubAPI.existsFile(repository, info.toolDescriptorDirectory, info.toolDescriptorName);

            info.toolLogoName = LOGO_FILE_NAME;
            info.toolLogoDirectory = info.toolDirectory;
            info.toolLogoPath = info.toolLogoDirectory + SEPARATOR + LOGO_FILE_NAME;
            info.existsToolLogo = GithubAPI.existsFile(repository, info.toolLogoDirectory, LOGO_FILE_NAME);

            info.executionContextsDirectory = info.toolLogoDirectory + SEPARATOR + EXECUTION_CONTEXTS_SUB_DIR;
            info.executionContextsNames = getExecutionContextsNames(info);
            info.executionContextsPath = info.executionContextsNames
                .stream()
                .map(name -> info.executionContextsDirectory + SEPARATOR + name)
                .collect(Collectors.toList());

            info.serializationFormat = Serialization.getFormatFromFileExtension(IOUtils.getExtensionFromFilePath(info.toolDescriptorPath));

            return info;
        } catch (DSLCoreException | IOException e) {
            throw new ToolsRepositoryException("Error checking info for tool " + toolName + "!", e);
        }
    }

    private String getDescriptorName(String toolDirectory) throws IOException, DSLCoreException {
        for(String file : GithubAPI.getFilesNames(repository, toolDirectory)) {
            if(file.startsWith(DESCRIPTOR_FILE_NAME))
                return file;
        }

        String extension = "." + Serialization.getFileExtensionFromFormat(serializationFormat);
        return DESCRIPTOR_FILE_NAME + extension;
    }

    private Collection<String> getExecutionContextsNames(ToolInfo info) throws IOException {
        if(!GithubAPI.existsFolder(repository, info.toolDirectory, EXECUTION_CONTEXTS_SUB_DIR))
            return new LinkedList<>();

        return GithubAPI.getFilesNames(repository, info.executionContextsDirectory);
    }

    private Collection<String> getToolsName() throws ToolsRepositoryException {
        try {
            return GithubAPI.getFoldersNames(repository);
        } catch(IOException e) {
            throw new ToolsRepositoryException("Error getting tools names!", e);
        }
    }

}
