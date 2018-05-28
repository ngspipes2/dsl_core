package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.support.ConfigSupportRepository;
import pt.isel.ngspipes.dsl_core.descriptors.utils.GithubUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.HttpUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.implementations.ToolsRepository;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class  GithubToolsRepository extends ToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String NAMES_KEY = "name";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String PARAMETER_API_QUERY = "?ref=master";
    private static final String SEPARATOR = "/";
    private static final String EXECUTION_CONTEXTS_SUB_URI = "/execution_contexts";


    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        if(!verifyLocation(location))
            return null;
        return new GithubToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws ToolsRepositoryException {
        try {
            if(isGithubUri(location)) {
                String apiLocation =  location.replace(
                        ConfigSupportRepository.github_base_uri,
                        ConfigSupportRepository.github_api_uri);
                apiLocation += "/contents";

                return HttpUtils.canConnect(apiLocation);
            }
        } catch (IOException e) {
            throw new ToolsRepositoryException("Could not verify location:" + location, e);
        }

        return false;
    }

    private static boolean isGithubUri(String location) {
        return location.startsWith(ConfigSupportRepository.github_base_uri);
    }


    private String accessUri;
    private String apiUri;


    public GithubToolsRepository(String location, Map<String, Object> config) {
        super(location, config);
        load();
    }

    // IMPLEMENTATION OF ToolsRepository
    @Override
    public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {

        Collection<String> names = getToolsName();
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }

    @Override
    public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
        try {
            String descriptorName = getDescriptorName(toolName);
            String toolDescriptorUri = getDescriptorUri(toolName, descriptorName);
            String content = HttpUtils.get(toolDescriptorUri);
            String type = IOUtils.getExtensionFromFilePath(descriptorName);

            return createToolDescriptor(toolName, type, content);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error loading " + toolName + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor tool) throws ToolsRepositoryException {
        throw new ToolsRepositoryException("Update operation not supported");
    }

    @Override
    public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
        throw new ToolsRepositoryException("Insert operation not supported");
    }

    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
        throw new ToolsRepositoryException("Delete operation not supported");
    }



    private Collection<String> getToolsName() throws ToolsRepositoryException {
        try {
            return GithubUtils.getFoldersNames(apiUri, NAMES_KEY);
        } catch(IOException e) {
            throw new ToolsRepositoryException("Error getting tools names!", e);
        }
    }

    private String getDescriptorUri(String toolName, String descriptorName) {
        String toolUri = accessUri + SEPARATOR + toolName;
        return toolUri + SEPARATOR + descriptorName;
    }

    private IToolDescriptor createToolDescriptor(String toolName, String type, String content) throws IOException, ToolsRepositoryException {
        ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsUtils.createToolDescriptor(content, type);
        Collection<IExecutionContextDescriptor> executionContextDescriptors = createExecutionContexts(toolName);
        toolDescriptor.setExecutionContexts(executionContextDescriptors);
        toolDescriptor.setLogo(getLogo(toolName));
        return toolDescriptor;
    }

    private String getDescriptorName(String toolName) throws IOException, ToolsRepositoryException {
        String descriptorName;
        String toolUri = getApiToolUri(toolName);
        Collection<String> names = GithubUtils.getFilesNames(toolUri, NAMES_KEY);
        for (String currName: names) {
            String type = IOUtils.getExtensionFromFilePath(currName);
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("." + type));
            if(currName.equals(DESCRIPTOR_FILE_NAME)) {
                return descriptorName;
            }
        }

        throw new ToolsRepositoryException("Couldn't find descriptor for tool" + toolName);
    }

    private String getApiToolUri(String toolName) {
        return apiUri + SEPARATOR + toolName + PARAMETER_API_QUERY;
    }

    private String getApiExecutionContextUri(String toolName) {
        return apiUri + SEPARATOR + toolName + EXECUTION_CONTEXTS_SUB_URI + PARAMETER_API_QUERY;
    }

    private String getAccessToolUri(String toolName) {
        return accessUri + SEPARATOR + toolName;
    }

    private byte[] getLogo(String toolName) throws IOException {
        String logoUri = getAccessToolUri(toolName) + SEPARATOR + LOGO_FILE_NAME;

        if(HttpUtils.canConnect(logoUri))
            return HttpUtils.getBytes(logoUri);

        return null;
    }

    private Collection<IExecutionContextDescriptor> createExecutionContexts(String toolName) throws IOException, ToolsRepositoryException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        Collection<String> names = getExecutionContextsNames(toolName);
        String executionAccessUri =  getAccessToolUri(toolName) + EXECUTION_CONTEXTS_SUB_URI;
        String uriCtx;

        for (String name: names) {
            uriCtx = executionAccessUri + SEPARATOR + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            String content = HttpUtils.get(uriCtx);
            IExecutionContextDescriptor context = ToolsDescriptorsUtils.createExecutionContextDescriptor(content, type);
            contexts.add(context);
        }

        return contexts;
    }

    private Collection<String> getExecutionContextsNames(String toolName) throws IOException {
        return GithubUtils.getFilesNames(getApiExecutionContextUri(toolName), NAMES_KEY);
    }

    private void load() {
        setAccessUri();
        setApiUri();
    }

    private void setAccessUri() {
        accessUri = location.replace(ConfigSupportRepository.github_base_uri, ConfigSupportRepository.github_access_uri);
        accessUri = accessUri + "/master";
    }

    private void setApiUri() {
        apiUri = location.replace(ConfigSupportRepository.github_base_uri, ConfigSupportRepository.github_api_uri);
        apiUri = apiUri + "/contents";
    }

}
