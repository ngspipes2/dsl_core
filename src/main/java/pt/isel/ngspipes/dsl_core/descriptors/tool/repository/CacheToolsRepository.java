package pt.isel.ngspipes.dsl_core.descriptors.tool.repository;

import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CacheToolsRepository implements IToolsRepository {

    private final IToolsRepository source;
    private final Map<String, IToolDescriptor> cache;
    private final Object lock;

    public CacheToolsRepository(IToolsRepository source) {
        this.source = source;
        this.cache = new HashMap<>();
        this.lock = new Object();
    }


    @Override
    public String getLocation() throws ToolsRepositoryException {
        return source.getLocation();
    }

    @Override
    public Map<String, Object> getConfig() throws ToolsRepositoryException {
        return source.getConfig();
    }

    @Override
    public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {
        synchronized (lock) {
            Collection<IToolDescriptor> toolsDescriptors = source.getAll();

            for (IToolDescriptor toolDescriptor : toolsDescriptors)
                cache.put(toolDescriptor.getName(), toolDescriptor);

            return toolsDescriptors;
        }
    }

    @Override
    public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
       synchronized (lock) {
           IToolDescriptor toolDescriptor = cache.get(toolName);

           if (toolDescriptor == null) {
               toolDescriptor = source.get(toolName);
               cache.put(toolName, toolDescriptor);
           }

           return toolDescriptor;
       }
    }

    @Override
    public void update(IToolDescriptor tool) throws ToolsRepositoryException {
        synchronized (lock) {
            source.update(tool);
            cache.put(tool.getName(), tool);
        }
    }

    @Override
    public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
        synchronized (lock) {
            source.insert(tool);
            cache.put(tool.getName(), tool);
        }
    }

    @Override
    public void delete(String toolName) throws ToolsRepositoryException {
        synchronized (lock) {
            source.delete(toolName);
            cache.remove(toolName);
        }
    }

}
