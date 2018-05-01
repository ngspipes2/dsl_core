package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import implementations.ToolsRepository;
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

public class LocalToolsRepository extends ToolsRepository {

    private static final String LOGO_FILE_NAME = "Logo.png";
    private static final String DESCRIPTOR_FILE_NAME  = "Descriptor";
    private static final String REPO_SUPPORT_LABEL_WIN = "LocalWindows";
    private static final String REPO_SUPPORT_LABEL_LX = "LocalLinux";

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
        SupportedRepository supportedRepositoryWindows = getAssociatedSupportedRepositoryInfo(location, REPO_SUPPORT_LABEL_WIN);
        SupportedRepository supportedRepositoryLinux = getAssociatedSupportedRepositoryInfo(location, REPO_SUPPORT_LABEL_LX);
        if(supportedRepositoryWindows == null && supportedRepositoryLinux == null)
            throw new ToolRepositoryException("Not supported repository");
        if(!location.endsWith("/"))
                location += "/";
    }

}
