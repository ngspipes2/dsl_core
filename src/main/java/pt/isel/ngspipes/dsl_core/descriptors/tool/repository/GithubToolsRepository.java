package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import implementations.ToolsRepository;
import interfaces.IToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.HttpUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.SupportedRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsFactoryUtils;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.configurator.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.tool.IToolDescriptor;
import utils.ToolRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static pt.isel.ngspipes.dsl_core.descriptors.tool.utils.RepositoryValidation.getAssociatedSupportedRepositoryInfo;

public class GithubToolsRepository extends ToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String NAMES_KEY = "name";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String REPO_SUPPORT_LABEL = "Github";
    private static final String PARAMETER_API_QUERY = "?ref=master";
    private static final String IGNORE_NAME = "LICENSE";

    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolRepositoryException {
        if(!verifyLocation(location, config))
            return null;
        return new GithubToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location, Map<String, Object> config) {
        try {
            SupportedRepository supportedRepository = getAssociatedSupportedRepositoryInfo(location, REPO_SUPPORT_LABEL);
            if(supportedRepository != null) {
                String apiLocation =  location.replace(supportedRepository.getBase_location(), supportedRepository.getApi_location());
                apiLocation = apiLocation + "/contents";
                if(HttpUtils.canConnect(apiLocation))
                    return true;
            }
        } catch(ToolRepositoryException exp) {}
        return false;
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

        Collection<String> names = HttpUtils.getJsonFieldsValuesFromArray(apiLocation, NAMES_KEY);
        names.remove(IGNORE_NAME);
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }

    @Override
    public IToolDescriptor get(String name) throws ToolRepositoryException {
        String uri = apiLocation + "/" + name + PARAMETER_API_QUERY;
        Collection<String> names = HttpUtils.getJsonFieldsValuesFromArray(uri, NAMES_KEY);
        String descriptorName = "";
        for (String currName: names) {
            if(currName.contains(DESCRIPTOR_FILE_NAME)) {
                descriptorName = currName;
                break;
            }
        }
        uri = accessLocation + "/" + name;
        String uriTool = uri + "/" + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        String content = HttpUtils.getContent(uriTool);
        try {
            ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsFactoryUtils.getToolDescriptor(content, type);
            Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(uri);
            toolDescriptor.setExecutionContexts(executionContextDescriptors);
            return toolDescriptor;
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading " + name + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor entity) throws ToolRepositoryException {
        throw new ToolRepositoryException("Not supported update operation");
    }

    @Override
    public void insert(IToolDescriptor entity) throws ToolRepositoryException {
        throw new ToolRepositoryException("Not supported insert operation");
    }

    @Override
    public void delete(String id) throws ToolRepositoryException {
        throw new ToolRepositoryException("Not supported delete operation");
    }




    private Collection<IExecutionContextDescriptor> getExecutionContexts(String uri) throws IOException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        uri += "/execution_contexts";
        String uriApi = uri.replace(accessLocation, apiLocation) + PARAMETER_API_QUERY;
        Collection<String> names = HttpUtils.getJsonFieldsValuesFromArray(uriApi, NAMES_KEY);
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
        SupportedRepository supportedRepository = getAssociatedSupportedRepositoryInfo(location, REPO_SUPPORT_LABEL);
        if(supportedRepository == null)
            throw new ToolRepositoryException("Not supported repository");
        setAccessLocation(supportedRepository);
        setApiLocation(supportedRepository);
    }

    private void setAccessLocation(SupportedRepository supportedRepository) {
        if(supportedRepository.getAccess_location() != null && !supportedRepository.getAccess_location().isEmpty()) {
            accessLocation = location.replace(supportedRepository.getBase_location(), supportedRepository.getAccess_location());
        }
        accessLocation = accessLocation + "/master";
    }

    private void setApiLocation(SupportedRepository supportedRepository) {
        if(supportedRepository.getAccess_location() != null && !supportedRepository.getApi_location().isEmpty()) {
            apiLocation = location.replace(supportedRepository.getBase_location(), supportedRepository.getApi_location());
        }
        apiLocation = apiLocation + "/contents";
    }
}
