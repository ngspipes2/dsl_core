package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import implementations.ToolsRepository;
import interfaces.IToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsFactoryUtils;
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
    private static final String EXECUTION_CONTEXTS_SUB_URI = "/execution_contexts";


    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolRepositoryException {
        if(!verifyLocation(location))
            return null;
        return new LocalToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) throws ToolRepositoryException {
        return IOUtils.canLoadDirectory(location);
    }

    public LocalToolsRepository(String location, Map<String, Object> config) {
        this(location, config, DEFAULT_TYPE);
    }

    public LocalToolsRepository(String location, Map<String, Object> config, String type) {
        super(location, config, type);
        load();
    }

    @Override
    public Collection<IToolDescriptor> getAll() throws ToolRepositoryException {

        Collection<String> names = IOUtils.getSubDirectoriesName(location);
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }

    @Override
    public IToolDescriptor get(String name) throws ToolRepositoryException {
        String toolPath = location + name;
        String descriptorName = getDescriptorName(toolPath);
        String toolDescriptorPath = toolPath + SEPARATOR + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        try {
            return getToolDescriptor(toolPath, toolDescriptorPath, type);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading " + name + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor entity) throws ToolRepositoryException {
        String toolPath = location + entity.getName();

        if (!IOUtils.canLoadDirectory(toolPath))
            throw new ToolRepositoryException("Can't find a tool with name: " + entity.getName());

        String descriptorName = getDescriptorName(toolPath);
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        String toolDescriptorPath = toolPath + SEPARATOR + descriptorName;

        try {
            updateTool(entity, toolPath, type, toolDescriptorPath);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error updating " + entity.getName() + " tool descriptor", e);
        }
    }

    @Override
    public void insert(IToolDescriptor entity) throws ToolRepositoryException {
        String toolPath = location + entity.getName();

        if(IOUtils.canLoadDirectory(toolPath))
           throw new ToolRepositoryException("There is already a tool with name: " + entity.getName());

        IOUtils.createFolder(toolPath);
        String toolDescriptorPath = toolPath + SEPARATOR + DESCRIPTOR_FILE_NAME + "." + type;
        try {
            insertTool(entity, toolPath, toolDescriptorPath);
        } catch (IOException e) {
            throw new ToolRepositoryException("Error updating " + entity.getName() + " tool descriptor", e);
        }
    }

    @Override
    public void delete(String id) throws ToolRepositoryException {
        IOUtils.deleteFolder(location + id);
    }




    private void updateTool(IToolDescriptor entity, String toolPath, String type, String toolDescriptorPath) throws IOException {
        String jsonStr = ToolsDescriptorsFactoryUtils.getToolDescriptorAsString(entity, type);
        IOUtils.write(jsonStr, toolDescriptorPath);

        Collection<IExecutionContextDescriptor> executionContexts = entity.getExecutionContexts();
        updateExecutionContexts(toolPath, executionContexts);

        IOUtils.copyFile(entity.getLogo(), toolPath + SEPARATOR + LOGO_FILE_NAME);
    }

    private void insertTool(IToolDescriptor entity, String toolPath, String toolDescriptorPath) throws IOException, ToolRepositoryException {
        String jsonStr = ToolsDescriptorsFactoryUtils.getToolDescriptorAsString(entity, type);
        IOUtils.write(jsonStr, toolDescriptorPath);

        Collection<IExecutionContextDescriptor> executionContexts = entity.getExecutionContexts();
        if(executionContexts == null || executionContexts.isEmpty())
            throw new ToolRepositoryException("A tool must have at least an execution context");
        writeExecutionContexts(toolPath, executionContexts);

        IOUtils.copyFile(entity.getLogo(), toolPath + SEPARATOR + LOGO_FILE_NAME);
    }

    private IToolDescriptor getToolDescriptor(String toolPath, String toolDescriptorPath, String type) throws IOException {
        String content = IOUtils.getContent(toolDescriptorPath);
        ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsFactoryUtils.createToolDescriptor(content, type);
        Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(toolPath);
        toolDescriptor.setExecutionContexts(executionContextDescriptors);
        toolDescriptor.setLogo(getLogo(toolPath));
        return toolDescriptor;
    }

    private void writeExecutionContexts(String toolPath, Collection<IExecutionContextDescriptor> executionContexts) throws IOException {
        String dirPath = toolPath + EXECUTION_CONTEXTS_SUB_URI;
        IOUtils.createFolder(dirPath);

        for (IExecutionContextDescriptor ctx : executionContexts) {
            String currCtx = ToolsDescriptorsFactoryUtils.getExecutionContextDescriptorAsString(ctx, type);
            IOUtils.write(currCtx, dirPath + SEPARATOR + ctx.getName() + "." + type);
        }
    }

    private void updateExecutionContexts(String toolPath, Collection<IExecutionContextDescriptor> executionContexts) throws IOException {
        String executionCtx = toolPath + EXECUTION_CONTEXTS_SUB_URI;
        Collection<String> names = IOUtils.getDirectoryFilesName(executionCtx);
        String pathCtx = "";

        for (String name: names) {
            pathCtx = executionCtx + "/" + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            IExecutionContextDescriptor ctx = getExecutionContextByName(executionContexts, name);
            String currCtx = ToolsDescriptorsFactoryUtils.getExecutionContextDescriptorAsString(ctx, type);
            IOUtils.write(currCtx, pathCtx);
        }
    }

    private IExecutionContextDescriptor getExecutionContextByName(Collection<IExecutionContextDescriptor> executionContexts, String name) {
        IExecutionContextDescriptor ctx = null;
        for (IExecutionContextDescriptor execCtx : executionContexts)
            if (execCtx.getName().equals(name))
                ctx = execCtx;
        return ctx;
    }

    private String getDescriptorName(String toolPath) {
        String descriptorName = "";
        Collection<String> names = IOUtils.getDirectoryFilesName(toolPath);
        for (String currName: names) {
            String type = IOUtils.getExtensionFromFilePath(currName);
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("." + type));
            if(currName.equals(DESCRIPTOR_FILE_NAME)) {
                break;
            }
        }
        return descriptorName;
    }

    private String getLogo(String toolPath) {
        StringBuilder logoUri = new StringBuilder(toolPath);
        logoUri.append(SEPARATOR)
                .append(LOGO_FILE_NAME);
        return IOUtils.canLoadFile(logoUri.toString()) ? logoUri.toString() : null;
    }

    private Collection<IExecutionContextDescriptor> getExecutionContexts(String path) throws IOException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        path += EXECUTION_CONTEXTS_SUB_URI;
        Collection<String> names = IOUtils.getDirectoryFilesName(path);
        String pathCtx = "";

        for (String name: names) {
            pathCtx = path + SEPARATOR + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            String content = IOUtils.getContent(pathCtx);
            IExecutionContextDescriptor context = ToolsDescriptorsFactoryUtils.createExecutionContextDescriptor(content, type);
            contexts.add(context);
        }

        return contexts;
    }

    private void load() {
        if(!location.endsWith(SEPARATOR))
                location += SEPARATOR;
    }

}
