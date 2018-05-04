package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import implementations.ToolsRepository;
import interfaces.IToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.GithubUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.HttpUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsFactoryUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.support.ConfigSupportRepository;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class GithubToolsRepository extends ToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String NAMES_KEY = "name";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String PARAMETER_API_QUERY = "?ref=master";

    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) {
        if(!verifyLocation(location))
            return null;
        return new GithubToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) {
        if(isGithubUri(location)) {
            String apiLocation =  location.replace( ConfigSupportRepository.github_base_location,
                    ConfigSupportRepository.github_api_location);
            apiLocation += "/contents";
            if(HttpUtils.canConnect(apiLocation))
                return true;
        }
        return false;
    }

    private static boolean isGithubUri(String location) {
        return location.startsWith(ConfigSupportRepository.github_base_location);
    }


    String accessLocation;
    String apiLocation;


    public GithubToolsRepository(String location, Map<String, Object> config) {
        super(location, config);
        load();
    }

    // IMPLEMENTATION OF ToolsRepository
    @Override
    public Collection<IToolDescriptor> getAll() throws ToolRepositoryException {

        Collection<String> names = GithubUtils.getFoldersNames(apiLocation, NAMES_KEY);
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }

    @Override
    public IToolDescriptor get(String name) throws ToolRepositoryException {
        String toolUri = apiLocation + "/" + name + PARAMETER_API_QUERY;
        Collection<String> names = GithubUtils.getFilesNames(toolUri, NAMES_KEY);
        String descriptorName = "";
        for (String currName: names) {
            if(currName.contains(DESCRIPTOR_FILE_NAME)) {
                descriptorName = currName;
                break;
            }
        }
        toolUri = accessLocation + "/" + name;
        String toolDescriptorUri = toolUri + "/"+ descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        String content = HttpUtils.getContent(toolDescriptorUri);
        try {
            ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsFactoryUtils.getToolDescriptor(content, type);
            Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(toolUri);
            toolDescriptor.setExecutionContexts(executionContextDescriptors);
            toolDescriptor.setLogo(getLogo(toolUri));
            return toolDescriptor;
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading " + name + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor entity) throws ToolRepositoryException {
        throw new ToolRepositoryException("Update operation not supported");
    }

    @Override
    public void insert(IToolDescriptor entity) throws ToolRepositoryException {
        throw new ToolRepositoryException("Insert operation not supported");
    }

    @Override
    public void delete(String id) throws ToolRepositoryException {
        throw new ToolRepositoryException("Delete operation not supported");
    }




    private String getLogo(String toolUri) {
        String logoUri = toolUri + "/" + LOGO_FILE_NAME;
        return HttpUtils.canConnect(logoUri) ? logoUri : null;
    }

    private Collection<IExecutionContextDescriptor> getExecutionContexts(String uri) throws IOException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        uri += "/execution_contexts" ;
        String uriApi = uri.replace(accessLocation, apiLocation) + PARAMETER_API_QUERY;
        Collection<String> names = GithubUtils.getFilesNames(uriApi, NAMES_KEY);
        String uriCtx = "";

        for (String name: names) {
            uriCtx = uri + "/" + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            String content = HttpUtils.getContent(uriCtx);
            IExecutionContextDescriptor context = ToolsDescriptorsFactoryUtils.getExecutionContextDescriptor(content, type);
            contexts.add(context);
        }

        return contexts;
    }

    private void load() {
        if(!HttpUtils.canConnect(location))
            throw new ToolRepositoryException("Cant load uri " + location);
        setAccessLocation();
        setApiLocation();
    }

    private void setAccessLocation() {
        accessLocation = location.replace(ConfigSupportRepository.github_base_location, ConfigSupportRepository.github_access_location);
        accessLocation = accessLocation + "/master";
    }

    private void setApiLocation() {
        apiLocation = location.replace(ConfigSupportRepository.github_base_location, ConfigSupportRepository.github_api_location);
        apiLocation = apiLocation + "/contents";
    }
}
