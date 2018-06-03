package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.GithubAPI;
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
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String SEPARATOR = "/";
    private static final String EXECUTION_CONTEXTS_SUB_URI = "execution_contexts";


    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        if(!verifyLocation(location))
            return null;
        return new GithubToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws ToolsRepositoryException {
        try {
            if(GithubAPI.isGithubUri(location))
                return GithubAPI.existsRepository(location);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Could not verify location:" + location, e);
        }

        return false;
    }



    public GithubToolsRepository(String location, Map<String, Object> config) {
        super(location, config);
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
            if(!getToolsName().contains(toolName))
                return null;

            String descriptorName = getDescriptorName(toolName);
            String descriptorPath = toolName + SEPARATOR + descriptorName;
            String content = GithubAPI.getFileContent(location, descriptorPath);
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
            return GithubAPI.getFoldersNames(location);
        } catch(IOException e) {
            throw new ToolsRepositoryException("Error getting tools names!", e);
        }
    }

    private IToolDescriptor createToolDescriptor(String toolName, String type, String content) throws IOException, ToolsRepositoryException {
        ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsUtils.createToolDescriptor(content, type);
        Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(toolName);
        toolDescriptor.setExecutionContexts(executionContextDescriptors);
        toolDescriptor.setLogo(getLogo(toolName));
        return toolDescriptor;
    }

    private String getDescriptorName(String toolName) throws IOException, ToolsRepositoryException {
        String descriptorName;
        Collection<String> names = GithubAPI.getFilesNames(location, toolName);
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

    private byte[] getLogo(String toolName) throws IOException {
        Collection<String> files = GithubAPI.getFilesNames(location, toolName);

        if(files.contains(LOGO_FILE_NAME))
            return GithubAPI.getFileBytes(location, toolName + SEPARATOR + LOGO_FILE_NAME);

        return null;
    }

    private Collection<IExecutionContextDescriptor> getExecutionContexts(String toolName) throws IOException, ToolsRepositoryException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        Collection<String> names = getExecutionContextsNames(toolName);
        String contextPath;

        for (String name: names) {
            contextPath = toolName + SEPARATOR  + EXECUTION_CONTEXTS_SUB_URI + SEPARATOR + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            String content = GithubAPI.getFileContent(location, contextPath);
            IExecutionContextDescriptor context = ToolsDescriptorsUtils.createExecutionContextDescriptor(content, type);
            contexts.add(context);
        }

        return contexts;
    }

    private Collection<String> getExecutionContextsNames(String toolName) throws IOException {
        String directory = toolName + SEPARATOR + EXECUTION_CONTEXTS_SUB_URI;
        return GithubAPI.getFilesNames(location, directory);
    }

}
