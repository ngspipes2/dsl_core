package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import implementations.ToolsRepository;
import interfaces.IToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsFactoryUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;


public class LocalToolsRepository extends ToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String DEFAULT_TYPE = "json";
    private static final String SEPARATOR = "/";
    private static final String EXECUTION_CONTEXTS_DIRECTORY = "/execution_contexts";


    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolRepositoryException {
        if(!verifyLocation(location))
            return null;
        return new LocalToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws ToolRepositoryException {
        return IOUtils.existDirectory(location);
    }

    public LocalToolsRepository(String location, Map<String, Object> config) {
        this(location, config, DEFAULT_TYPE);
    }

    public LocalToolsRepository(String location, Map<String, Object> config, String type) {
        super(location, config, type);
    }

    @Override
    public Collection<IToolDescriptor> getAll() throws ToolRepositoryException {

        Collection<String> names = IOUtils.getSubDirectoriesName(location + SEPARATOR);
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }

    @Override
    public IToolDescriptor get(String toolName) throws ToolRepositoryException {
        String toolPath = location + SEPARATOR + toolName;
        String descriptorName = getDescriptorName(toolPath);
        String toolDescriptorPath = toolPath + SEPARATOR + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        try {
            return getToolDescriptor(toolPath, toolDescriptorPath, type);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading " + toolName + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor tool) throws ToolRepositoryException {
        delete(tool.getName());
        insert(tool);
    }

    @Override
    public void insert(IToolDescriptor tool) throws ToolRepositoryException {
        String toolPath = location + SEPARATOR + tool.getName();

        if(IOUtils.existDirectory(toolPath))
           throw new ToolRepositoryException("There is already a tool with name: " + tool.getName());

        IOUtils.createFolder(toolPath);
        String toolDescriptorPath = toolPath + SEPARATOR + DESCRIPTOR_FILE_NAME + "." + type;
        try {
            insertTool(tool, toolPath, toolDescriptorPath);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error updating " + tool.getName() + " tool descriptor", e);
        }
    }

    @Override
    public void delete(String toolName) {
        IOUtils.deleteFolder(location + SEPARATOR + toolName);
    }



    private void insertTool(IToolDescriptor tool, String toolPath, String toolDescriptorPath) throws IOException, ToolRepositoryException {
        String descriptorAsString = ToolsDescriptorsFactoryUtils.getToolDescriptorAsString(tool, type);
        IOUtils.write(descriptorAsString, toolDescriptorPath);

        Collection<IExecutionContextDescriptor> executionContexts = tool.getExecutionContexts();
        writeExecutionContexts(toolPath, executionContexts);

        IOUtils.writeBytes(tool.getLogo(), toolPath + SEPARATOR + LOGO_FILE_NAME);
    }

    private IToolDescriptor getToolDescriptor(String toolPath, String toolDescriptorPath, String type) throws IOException, ToolRepositoryException {
        String content = IOUtils.read(toolDescriptorPath);
        ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsFactoryUtils.createToolDescriptor(content, type);
        Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(toolPath);
        toolDescriptor.setExecutionContexts(executionContextDescriptors);
        toolDescriptor.setLogo(getLogo(toolPath));
        return toolDescriptor;
    }

    private void writeExecutionContexts(String toolPath, Collection<IExecutionContextDescriptor> executionContexts) throws IOException, ToolRepositoryException {
        String dirPath = toolPath + EXECUTION_CONTEXTS_DIRECTORY;
        IOUtils.createFolder(dirPath);

        for (IExecutionContextDescriptor ctx : executionContexts) {
            String currCtx = ToolsDescriptorsFactoryUtils.getExecutionContextDescriptorAsString(ctx, type);
            IOUtils.write(currCtx, dirPath + SEPARATOR + ctx.getName() + "." + type);
        }
    }

    private String getDescriptorName(String toolPath) throws ToolRepositoryException {
        String descriptorName = "";
        Collection<String> names = IOUtils.getDirectoryFilesName(toolPath);
        for (String currName: names) {
            String type = IOUtils.getExtensionFromFilePath(currName);
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("." + type));
            if(currName.equals(DESCRIPTOR_FILE_NAME)) {
                return descriptorName;
            }
        }
        throw new ToolRepositoryException("Couldn't find descriptor for tool");
    }

    private byte[] getLogo(String toolPath) throws IOException {
        String logoPath = toolPath + SEPARATOR + LOGO_FILE_NAME;

        if(IOUtils.existFile(logoPath))
            return IOUtils.readBytes(logoPath);

        return null;

    }

    private Collection<IExecutionContextDescriptor> getExecutionContexts(String path) throws IOException, ToolRepositoryException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        path += EXECUTION_CONTEXTS_DIRECTORY;
        Collection<String> names = IOUtils.getDirectoryFilesName(path);
        String pathCtx;

        for (String name: names) {
            pathCtx = path + SEPARATOR + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            String content = IOUtils.read(pathCtx);
            IExecutionContextDescriptor context = ToolsDescriptorsFactoryUtils.createExecutionContextDescriptor(content, type);
            contexts.add(context);
        }

        return contexts;
    }

}
