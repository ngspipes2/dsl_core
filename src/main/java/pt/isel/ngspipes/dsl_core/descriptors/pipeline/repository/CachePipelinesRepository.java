package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CachePipelinesRepository implements IPipelinesRepository {

    private final Object lock;
    private final IPipelinesRepository source;
    private final Map<String, IPipelineDescriptor> cache;
    private byte[] logo;



    public CachePipelinesRepository(IPipelinesRepository source) {
        this.source = source;
        this.cache = new HashMap<>();
        this.lock = new Object();
    }



    @Override
    public String getLocation() throws PipelinesRepositoryException {
        return source.getLocation();
    }

    @Override
    public Map<String, Object> getConfig() throws PipelinesRepositoryException {
        return source.getConfig();
    }

    @Override
    public byte[] getLogo() throws PipelinesRepositoryException{
        if(logo == null)
            logo = source.getLogo();

        return logo;
    }

    @Override
    public void setLogo(byte[] logo) throws PipelinesRepositoryException {
        source.setLogo(logo);
        this.logo = logo;
    }

    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        synchronized (lock) {
            Collection<IPipelineDescriptor> pipelinesDescriptors = source.getAll();

            for (IPipelineDescriptor pipelineDescriptor : pipelinesDescriptors)
                cache.put(pipelineDescriptor.getName(), pipelineDescriptor);

            return pipelinesDescriptors;
        }
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        synchronized (lock) {
            IPipelineDescriptor pipelineDescriptor = cache.get(pipelineName);

            if (pipelineDescriptor == null) {
                pipelineDescriptor = source.get(pipelineName);
                cache.put(pipelineName, pipelineDescriptor);
            }

            return pipelineDescriptor;
        }
    }

    @Override
    public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        synchronized (lock) {
            source.insert(pipeline);
            cache.put(pipeline.getName(), pipeline);
        }
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        synchronized (lock) {
            source.update(pipeline);
            cache.put(pipeline.getName(), pipeline);
        }
    }

    @Override
    public void delete(String pipelineName) throws PipelinesRepositoryException {
        synchronized (lock) {
            source.delete(pipelineName);
            cache.remove(pipelineName);
        }
    }

}
