package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelineRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CachePipelinesRepository implements IPipelinesRepository {


    private final IPipelinesRepository source;
    private final Map<String, IPipelineDescriptor> cache;
    private final Object lock;

    public CachePipelinesRepository(IPipelinesRepository source) {
        this.source = source;
        this.cache = new HashMap<>();
        this.lock = new Object();
    }

    @Override
    public String getLocation() throws PipelineRepositoryException {
        return source.getLocation();
    }

    @Override
    public Map<String, Object> getConfig() throws PipelineRepositoryException {
        return source.getConfig();
    }

    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelineRepositoryException {
        synchronized (lock) {
            Collection<IPipelineDescriptor> pipelinesDescriptors = source.getAll();

            for (IPipelineDescriptor pipelineDescriptor : pipelinesDescriptors)
                cache.put(pipelineDescriptor.getName(), pipelineDescriptor);

            return pipelinesDescriptors;
        }
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelineRepositoryException {
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
    public void insert(IPipelineDescriptor pipeline) throws PipelineRepositoryException {
        synchronized (lock) {
            source.insert(pipeline);
            cache.put(pipeline.getName(), pipeline);
        }
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelineRepositoryException {
        synchronized (lock) {
            source.update(pipeline);
            cache.put(pipeline.getName(), pipeline);
        }
    }

    @Override
    public void delete(String pipelineName) throws PipelineRepositoryException {
        synchronized (lock) {
            source.delete(pipelineName);
            cache.remove(pipelineName);
        }
    }
}
