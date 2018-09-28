package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.wrapper.WrapperToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.implementations.ToolsRepository;
import utils.ToolsRepositoryException;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WrapperToolsRepository extends ToolsRepository {

    public WrapperToolsRepository(String location, Map<String, Object> config) {
        super(location, config);
    }



    @Override
    public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {
        return getAllWrapped()
            .stream()
            .map((tool) -> {
                if(tool == null)
                    return null;

                tool =  new WrapperToolDescriptor(tool);
                return tool;
            })
            .collect(Collectors.toList());
    }

    @Override
    public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
        IToolDescriptor tool = getWrapped(toolName);

        if(tool == null)
            return null;

        tool =  new WrapperToolDescriptor(tool);
        return tool;
    }

    @Override
    public void update(IToolDescriptor tool) throws ToolsRepositoryException {
        tool =  new WrapperToolDescriptor(tool);
        updateWrapped(tool);
    }

    @Override
    public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
        tool =  new WrapperToolDescriptor(tool);
        insertWrapped(tool);
    }


    protected abstract Collection<IToolDescriptor> getAllWrapped() throws ToolsRepositoryException;
    protected abstract IToolDescriptor getWrapped(String toolName) throws ToolsRepositoryException;
    protected abstract void updateWrapped(IToolDescriptor tool) throws ToolsRepositoryException;
    protected abstract void insertWrapped(IToolDescriptor tool) throws ToolsRepositoryException;

}
