package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.ToolsDescriptorsUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.implementations.ToolsRepository;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;


public class LocalToolsRepository extends ToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String SEPARATOR = "/";
    private static final String EXECUTION_CONTEXTS_DIRECTORY = "/execution_contexts";


    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        if(!verifyLocation(location))
            return null;
        return new LocalToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) {
        return IOUtils.existDirectory(location);
    }



    private Serialization.Format serializationFormat;



    public LocalToolsRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_TOOL_SERIALIZATION_FORMAT);
    }

    public LocalToolsRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);
        this.serializationFormat = serializationFormat;
    }


    @Override
    public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {

        Collection<String> names = IOUtils.getSubDirectoriesName(location + SEPARATOR);
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }

    @Override
    public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
        String toolPath = location + SEPARATOR + toolName;
        String descriptorName = getDescriptorName(toolPath);
        String toolDescriptorPath = toolPath + SEPARATOR + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        try {
            return getToolDescriptor(toolPath, toolDescriptorPath, type);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error loading " + toolName + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor tool) throws ToolsRepositoryException {
        IToolDescriptor toolToUpdate = transformToJacksonToolDescriptor(tool);
        String toolPath = location + SEPARATOR + tool.getName();

        if (!IOUtils.existDirectory(toolPath))
            throw new ToolsRepositoryException("Can't find a tool with name: " + tool.getName());

        String descriptorName = getDescriptorName(toolPath);
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        String toolDescriptorPath = toolPath + SEPARATOR + descriptorName;

        try {
            updateTool(tool, toolPath, type, toolDescriptorPath);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error updating " + tool.getName() + " tool descriptor", e);
        }
    }

    @Override
    public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
        String toolPath = location + SEPARATOR + tool.getName();

        if(IOUtils.existDirectory(toolPath))
           throw new ToolsRepositoryException("There is already a tool with name: " + tool.getName());

        IOUtils.createFolder(toolPath);
        String toolDescriptorPath = toolPath + SEPARATOR + DESCRIPTOR_FILE_NAME + "." + getExtension();
        try {
            insertTool(tool, toolPath, toolDescriptorPath);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error updating " + tool.getName() + " tool descriptor", e);
        }
    }

    @Override
    public void delete(String toolName) {
        IOUtils.deleteFolder(location + SEPARATOR + toolName);
    }



    private JacksonToolDescriptor transformToJacksonToolDescriptor(IToolDescriptor tool) {
        Collection<ICommandDescriptor> commands = transformToJacksonCommandsDescriptors(tool.getCommands());
        tool.setCommands(commands);
        return new JacksonToolDescriptor(tool);
    }

    private Collection<ICommandDescriptor> transformToJacksonCommandsDescriptors(Collection<ICommandDescriptor> commands) {
        Collection<ICommandDescriptor> jacksonCommands = new LinkedList<>();

        if (commands == null)
            return jacksonCommands;

        for (ICommandDescriptor command : commands)
            jacksonCommands.add(transformToJacksonCommandDescriptor(command));

        return jacksonCommands;
    }

    private JacksonCommandDescriptor transformToJacksonCommandDescriptor(ICommandDescriptor command) {
        Collection<IParameterDescriptor> parameters = transformToJacksonParametersDescriptors(command.getParameters());
        command.setParameters(parameters);
        return new JacksonCommandDescriptor(command);
    }

    private Collection<IParameterDescriptor> transformToJacksonParametersDescriptors(Collection<IParameterDescriptor> parameters) {
        Collection<IParameterDescriptor> jacksonParameters = new LinkedList<>();

        if (parameters == null)
            return jacksonParameters;

        for (IParameterDescriptor parameter : parameters)
            jacksonParameters.add(transformToJacksonParameterDescriptor(parameter));

        return jacksonParameters;
    }

    private JacksonParameterDescriptor transformToJacksonParameterDescriptor(IParameterDescriptor parameter) {
        return new JacksonParameterDescriptor(parameter);
    }

    private void updateTool(IToolDescriptor tool, String toolPath, String type, String toolDescriptorPath) throws IOException, ToolsRepositoryException {
        String descriptorAsString = ToolsDescriptorsUtils.getToolDescriptorAsString(tool, type);
        IOUtils.write(descriptorAsString, toolDescriptorPath);

        Collection<IExecutionContextDescriptor> executionContexts = tool.getExecutionContexts();
        updateExecutionContexts(toolPath, executionContexts);

        IOUtils.writeBytes(tool.getLogo(), toolPath + SEPARATOR + LOGO_FILE_NAME);
    }

    private void insertTool(IToolDescriptor tool, String toolPath, String toolDescriptorPath) throws IOException, ToolsRepositoryException {
        String descriptorAsString = ToolsDescriptorsUtils.getToolDescriptorAsString(tool, serializationFormat);
        IOUtils.write(descriptorAsString, toolDescriptorPath);

        Collection<IExecutionContextDescriptor> executionContexts = tool.getExecutionContexts();
        if(executionContexts == null || executionContexts.isEmpty())
            throw new ToolsRepositoryException("A tool must have at least an execution context");
        writeExecutionContexts(toolPath, executionContexts);

        IOUtils.writeBytes(tool.getLogo(), toolPath + SEPARATOR + LOGO_FILE_NAME);
    }

    private IToolDescriptor getToolDescriptor(String toolPath, String toolDescriptorPath, String type) throws IOException, ToolsRepositoryException {
        String content = IOUtils.read(toolDescriptorPath);
        ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsUtils.createToolDescriptor(content, type);
        Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(toolPath);
        toolDescriptor.setExecutionContexts(executionContextDescriptors);
        toolDescriptor.setLogo(getLogo(toolPath));
        return toolDescriptor;
    }

    private void writeExecutionContexts(String toolPath, Collection<IExecutionContextDescriptor> executionContexts) throws IOException, ToolsRepositoryException {
        String dirPath = toolPath + EXECUTION_CONTEXTS_DIRECTORY;
        IOUtils.createFolder(dirPath);

        for (IExecutionContextDescriptor ctx : executionContexts) {
            String currCtx = ToolsDescriptorsUtils.getExecutionContextDescriptorAsString(ctx, serializationFormat);
            IOUtils.write(currCtx, dirPath + SEPARATOR + ctx.getName() + "." + getExtension());
        }
    }

    private void updateExecutionContexts(String toolPath, Collection<IExecutionContextDescriptor> executionContexts) throws IOException, ToolsRepositoryException {
        String executionCtx = toolPath + EXECUTION_CONTEXTS_DIRECTORY;
        Collection<String> names = IOUtils.getDirectoryFilesName(executionCtx);
        String pathCtx;

        for (String name: names) {
            pathCtx = executionCtx + "/" + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            IExecutionContextDescriptor ctx = getExecutionContextByName(executionContexts, name);
            String currCtx = ToolsDescriptorsUtils.getExecutionContextDescriptorAsString(ctx, type);
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

    private String getDescriptorName(String toolPath) throws ToolsRepositoryException {
        String descriptorName;
        Collection<String> names = IOUtils.getDirectoryFilesName(toolPath);
        for (String currName: names) {
            descriptorName = currName;
            currName = currName.substring(0, currName.indexOf("."));
            if(currName.equals(DESCRIPTOR_FILE_NAME)) {
                return descriptorName;
            }
        }
        throw new ToolsRepositoryException("Couldn't find descriptor for tool");
    }

    private byte[] getLogo(String toolPath) throws IOException {
        String logoPath = toolPath + SEPARATOR + LOGO_FILE_NAME;

        if(IOUtils.existFile(logoPath))
            return IOUtils.readBytes(logoPath);

        return null;
    }

    private Collection<IExecutionContextDescriptor> getExecutionContexts(String path) throws IOException, ToolsRepositoryException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        path += EXECUTION_CONTEXTS_DIRECTORY;
        Collection<String> names = IOUtils.getDirectoryFilesName(path);
        String pathCtx;

        for (String name: names) {
            pathCtx = path + SEPARATOR + name;
            String serializationType = IOUtils.getExtensionFromFilePath(name);
            String content = IOUtils.read(pathCtx);
            IExecutionContextDescriptor context = ToolsDescriptorsUtils.createExecutionContextDescriptor(content, serializationType);
            contexts.add(context);
        }

        return contexts;
    }

    private String getExtension() throws ToolsRepositoryException {
        try {
            return Serialization.getFileExtensionFromFormat(serializationFormat);
        } catch (DSLCoreException e) {
            throw new ToolsRepositoryException(e.getMessage(), e);
        }
    }

}
