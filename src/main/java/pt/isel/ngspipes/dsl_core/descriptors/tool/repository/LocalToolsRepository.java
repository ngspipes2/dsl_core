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


    // IMPLEMENTATION OF IToolRepositoryFactory
    public static IToolsRepository create(String location, Map<String, Object> config) {
        if(!verifyLocation(location))
            return null;
        return new LocalToolsRepository(location, config);
    }

    private static boolean verifyLocation(String location) {
        return IOUtils.canLoadDirectory(location);
    }

    public LocalToolsRepository(String location, Map<String, Object> config) {
        super(location, config);
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
        String descriptorName = "";
        String toolPath = location + name;
        Collection<String> names = IOUtils.getDirectoryFilesName(toolPath);
        for (String currName: names) {
            if(currName.contains(DESCRIPTOR_FILE_NAME)) {
                descriptorName = currName;
                break;
            }
        }

        String toolDescriptorPath = toolPath + "/" + descriptorName;
        String type = IOUtils.getExtensionFromFilePath(descriptorName);
        try {
            String content = IOUtils.getContent(toolDescriptorPath);
            ToolDescriptor toolDescriptor = (ToolDescriptor) ToolsDescriptorsFactoryUtils.getToolDescriptor(content, type);
            Collection<IExecutionContextDescriptor> executionContextDescriptors = getExecutionContexts(toolPath);
            toolDescriptor.setExecutionContexts(executionContextDescriptors);
            toolDescriptor.setLogo(getLogo(toolPath));
            return toolDescriptor;
        } catch (IOException e) {
            throw new ToolRepositoryException("Error loading " + name + " tool descriptor", e);
        }
    }

    @Override
    public void update(IToolDescriptor entity) throws ToolRepositoryException {

    }

    @Override
    public void insert(IToolDescriptor entity) throws ToolRepositoryException {

    }

    @Override
    public void delete(String id) throws ToolRepositoryException {

    }




    private String getLogo(String toolPath) {
        StringBuilder logoUri = new StringBuilder(toolPath);
        logoUri.append("/")
                .append(LOGO_FILE_NAME);
        return IOUtils.canLoadFile(logoUri.toString()) ? logoUri.toString() : null;
    }

    private Collection<IExecutionContextDescriptor> getExecutionContexts(String path) throws IOException {
        Collection<IExecutionContextDescriptor> contexts = new LinkedList<>();
        path += "/execution_contexts";
        Collection<String> names = IOUtils.getDirectoryFilesName(path);
        String pathCtx = "";

        for (String name: names) {
            pathCtx = path + "/" + name;
            String type = IOUtils.getExtensionFromFilePath(name);
            String content = IOUtils.getContent(pathCtx);
            IExecutionContextDescriptor context = ToolsDescriptorsFactoryUtils.getExecutionContextDescriptor(content, type);
            contexts.add(context);
        }

        return contexts;
    }

    private void load() {
        if(!location.endsWith("/"))
                location += "/";
    }

}
