package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryToolsRepository implements IToolsRepository {

    private final Object lock = new Object();
    private Map<String, IToolDescriptor> toolsByName = new HashMap<>();
    private byte[] logo;



    @Override
    public String getLocation() throws ToolsRepositoryException {
        return null;
    }

    @Override
    public Map<String, Object> getConfig() throws ToolsRepositoryException {
        return null;
    }

    @Override
    public byte[] getLogo() {
        return this.logo;
    }

    @Override
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    @Override
    public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {
        synchronized (lock) {
            return toolsByName.values();
        }
    }

    @Override
    public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
        synchronized (lock) {
            return toolsByName.get(toolName);
        }
    }

    @Override
    public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
        synchronized (lock) {
            if(toolsByName.containsKey(tool.getName()))
                throw new ToolsRepositoryException("There is already a tool with name:" + tool.getName());

            toolsByName.put(tool.getName(), tool);
        }
    }

    @Override
    public void update(IToolDescriptor tool) throws ToolsRepositoryException {
        synchronized (lock) {
            if(!toolsByName.containsKey(tool.getName()))
                throw new ToolsRepositoryException("There is not tool with name:" + tool.getName());

            toolsByName.put(tool.getName(), tool);
        }
    }

    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
        synchronized (lock) {
            toolsByName.remove(toolName);
        }
    }

}
