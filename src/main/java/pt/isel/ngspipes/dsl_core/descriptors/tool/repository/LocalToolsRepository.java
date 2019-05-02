package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.dsl_core.descriptors.Configuration;
import pt.isel.ngspipes.dsl_core.descriptors.exceptions.DSLCoreException;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.fileBased.FileBasedToolDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.utils.FileBasedToolDescriptorsUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.IOUtils;
import pt.isel.ngspipes.dsl_core.descriptors.utils.Serialization;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;


public class LocalToolsRepository extends WrapperToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String SEPARATOR = File.separatorChar + "";
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



    private Serialization.Format serializationFormat;



    public LocalToolsRepository(String location, Map<String, Object> config) {
        this(location, config, Configuration.DEFAULT_TOOL_SERIALIZATION_FORMAT);
    }

    public LocalToolsRepository(String location, Map<String, Object> config, Serialization.Format serializationFormat) {
        super(location, config);
        this.serializationFormat = serializationFormat;
    }



    @Override
    public byte[] getLogo() throws ToolsRepositoryException {
        String logoPath = super.location + "/" + LOGO_FILE_NAME;

        try {
            if(IOUtils.existFile(logoPath))
                return IOUtils.readBytes(logoPath);
            else
                return null;
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error getting logo!", e);
        }
    }

    @Override
    public void setLogo(byte[] logo) throws ToolsRepositoryException {
        String logoPath = super.location + "/" + LOGO_FILE_NAME;

        try {
            if(logo == null) {
                if(IOUtils.existFile(logoPath))
                    IOUtils.deleteFile(logoPath);
            } else {
                IOUtils.writeBytes(logo, logoPath);
            }
        } catch (IOException e) {
            throw new ToolsRepositoryException("Error setting logo!", e);
        }
    }


    @Override
    public Collection<String> getToolsNames() throws ToolsRepositoryException {
        return IOUtils.getSubDirectoriesName(location);
    }


    @Override
    protected Collection<IToolDescriptor> getAllWrapped() throws ToolsRepositoryException {
        Collection<String> names = IOUtils.getSubDirectoriesName(location);
        Collection<IToolDescriptor> tools = new LinkedList<>();

        for (String name: names)
            tools.add(get(name));

        return tools;
    }


    @Override
    protected IToolDescriptor getWrapped(String toolName) throws ToolsRepositoryException {
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
        String content = IOUtils.read(info.toolDescriptorPath);

        IToolDescriptor toolDescriptor = FileBasedToolDescriptorsUtils.createToolDescriptor(content, info.serializationFormat);

        return toolDescriptor;
    }

    private byte[] getToolLogo(ToolInfo info) throws IOException, ToolsRepositoryException {
        if(!info.existsToolLogo)
            return null;

        return IOUtils.readBytes(info.toolLogoPath);
    }

    private Collection<IExecutionContextDescriptor> getToolExecutionContexts(ToolInfo info) throws IOException, ToolsRepositoryException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();

        for (String contextPath : info.executionContextsPath) {
            String type = IOUtils.getExtensionFromFilePath(contextPath);

            String content = IOUtils.read(contextPath);

            IExecutionContextDescriptor context = FileBasedToolDescriptorsUtils.createExecutionContextDescriptor(content, type);

            contexts.add(context);
        }

        return contexts;
    }


    @Override
    protected void updateWrapped(IToolDescriptor tool) throws ToolsRepositoryException {
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
        FileBasedToolDescriptor fileBasedToolDescriptor = new FileBasedToolDescriptor(tool);

        String content = FileBasedToolDescriptorsUtils.getToolDescriptorAsString(fileBasedToolDescriptor, info.serializationFormat);

        IOUtils.write(content, info.toolDescriptorPath);
    }

    private void updateToolLogo(ToolInfo info, IToolDescriptor tool) throws IOException {
        if(tool.getLogo() != null)
            IOUtils.writeBytes(tool.getLogo(), info.toolLogoPath);
        else if(info.existsToolLogo)
            IOUtils.deleteFile(info.toolLogoPath);
    }

    private void updateToolExecutionContexts(ToolInfo info, IToolDescriptor tool) throws IOException, DSLCoreException, ToolsRepositoryException {
        String extension = "." + Serialization.getFileExtensionFromFormat(info.serializationFormat);

        String content;
        String path;
        for(IExecutionContextDescriptor executionContext : tool.getExecutionContexts()) {
            content = FileBasedToolDescriptorsUtils.getExecutionContextDescriptorAsString(executionContext, info.serializationFormat);
            path = info.executionContextsDirectory + SEPARATOR + executionContext.getName() + extension;

            IOUtils.write(content, path);
        }

        for(String fileName : info.executionContextsNames) {
            if(tool.getExecutionContexts()
                    .stream()
                    .noneMatch(ec -> ec.getName().equals(fileName.split("\\.")[0])))
                IOUtils.deleteFile(info.executionContextsDirectory + SEPARATOR + fileName);
        }
    }


    @Override
    protected void insertWrapped(IToolDescriptor tool) throws ToolsRepositoryException {
        ToolInfo toolInfo = createToolInfo(tool.getName());

        if(toolInfo.existsTool)
            throw new ToolsRepositoryException("There is already a tool with name: " + tool.getName());

        insert(toolInfo, tool);
    }

    private void insert(ToolInfo info, IToolDescriptor tool) throws ToolsRepositoryException {
        try {
            IOUtils.createFolder(info.toolDirectory);
            IOUtils.createFolder(info.executionContextsDirectory);

            insertToolDescriptor(info, tool);
            insertToolLogo(info, tool);
            insertToolExecutionContexts(info, tool);
        } catch (DSLCoreException | IOException e) {
            throw new ToolsRepositoryException("Error writing " + tool.getName() + " tool descriptor", e);
        }
    }

    private void insertToolDescriptor(ToolInfo info, IToolDescriptor tool) throws IOException, ToolsRepositoryException {
        FileBasedToolDescriptor fileBasedToolDescriptor = new FileBasedToolDescriptor(tool);

        String content = FileBasedToolDescriptorsUtils.getToolDescriptorAsString(fileBasedToolDescriptor, info.serializationFormat);

        IOUtils.write(content, info.toolDescriptorPath);
    }

    private void insertToolLogo(ToolInfo info, IToolDescriptor tool) throws IOException {
        if(tool.getLogo() != null)
            IOUtils.writeBytes(tool.getLogo(), info.toolLogoPath);
    }

    private void insertToolExecutionContexts(ToolInfo info, IToolDescriptor tool) throws IOException, ToolsRepositoryException, DSLCoreException {
        String extension = "." + Serialization.getFileExtensionFromFormat(info.serializationFormat);

        String content;
        String path;
        for(IExecutionContextDescriptor executionContext : tool.getExecutionContexts()) {
            content = FileBasedToolDescriptorsUtils.getExecutionContextDescriptorAsString(executionContext, info.serializationFormat);
            path = info.executionContextsDirectory + SEPARATOR + executionContext.getName() + extension;
            IOUtils.write(content, path);
        }
    }


    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
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

            IOUtils.deleteFolder(info.executionContextsDirectory);
            IOUtils.deleteFolder(info.toolDirectory);
        } catch (IOException e) {
            throw new ToolsRepositoryException("Could not delete folder " + info.toolDirectory + "!", e);
        }
    }

    private void deleteToolDescriptor(ToolInfo info) throws IOException {
        IOUtils.deleteFile(info.toolDescriptorPath);
    }

    private void deleteToolLogo(ToolInfo info) throws IOException {
        if(info.existsToolLogo)
            IOUtils.deleteFile(info.toolLogoPath);
    }

    private void deleteToolExecutionContexts(ToolInfo info) throws IOException {
        for(String path : info.executionContextsPath)
            IOUtils.deleteFile(path);
    }


    private ToolInfo createToolInfo(String toolName) throws ToolsRepositoryException {
        try {
            ToolInfo info = new ToolInfo();

            info.toolName = toolName;
            info.toolDirectory = super.location + SEPARATOR + toolName;
            info.existsTool = IOUtils.existDirectory(info.toolDirectory);

            info.toolDescriptorName = getDescriptorName(info.toolDirectory);
            info.toolDescriptorDirectory = info.toolDirectory;
            info.toolDescriptorPath = info.toolDescriptorDirectory + SEPARATOR + info.toolDescriptorName;
            info.existsToolDescriptor = IOUtils.existFile(info.toolDescriptorPath);

            info.toolLogoName = LOGO_FILE_NAME;
            info.toolLogoDirectory = info.toolDirectory;
            info.toolLogoPath = info.toolLogoDirectory + SEPARATOR + LOGO_FILE_NAME;
            info.existsToolLogo = IOUtils.existFile(info.toolLogoPath);

            info.executionContextsDirectory = info.toolLogoDirectory + SEPARATOR + EXECUTION_CONTEXTS_DIRECTORY;
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
        for(String file : IOUtils.getDirectoryFilesName(toolDirectory)) {
            if(file.startsWith(DESCRIPTOR_FILE_NAME))
                return file;
        }

        String extension = "." + Serialization.getFileExtensionFromFormat(serializationFormat);
        return DESCRIPTOR_FILE_NAME + extension;
    }

    private Collection<String> getExecutionContextsNames(ToolInfo info) throws IOException {
        return IOUtils.getDirectoryFilesName(info.executionContextsDirectory);
    }

}
