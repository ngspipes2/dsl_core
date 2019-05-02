package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryPipelinesRepository implements IPipelinesRepository {

    private final Object lock = new Object();
    private Map<String, IPipelineDescriptor> pipelinesByName = new HashMap<>();
    private byte[] logo;



    @Override
    public String getLocation() throws PipelinesRepositoryException {
        return null;
    }

    @Override
    public Map<String, Object> getConfig() throws PipelinesRepositoryException {
        return null;
    }


    @Override
    public byte[] getLogo() {
        return logo;
    }

    @Override
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }


    @Override
    public Collection<String> getPipelinesNames() throws PipelinesRepositoryException {
        return pipelinesByName.keySet();
    }


    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        synchronized (lock) {
            return pipelinesByName.values();
        }
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        synchronized (lock) {
            return pipelinesByName.get(pipelineName);
        }
    }

    @Override
    public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        synchronized (lock) {
            if(pipelinesByName.containsKey(pipeline.getName()))
                throw new PipelinesRepositoryException("There is already a pipeline with name:" + pipeline.getName());

            pipelinesByName.put(pipeline.getName(), pipeline);
        }
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        synchronized (lock) {
            if(!pipelinesByName.containsKey(pipeline.getName()))
                throw new PipelinesRepositoryException("There is not pipeline with name:" + pipeline.getName());

            pipelinesByName.put(pipeline.getName(), pipeline);
        }
    }

    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        synchronized (lock) {
            pipelinesByName.remove(pipelineName);
        }
    }

}
