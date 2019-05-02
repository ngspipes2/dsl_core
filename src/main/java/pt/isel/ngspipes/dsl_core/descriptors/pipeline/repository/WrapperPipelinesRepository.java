package pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.WrapperPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class WrapperPipelinesRepository extends PipelinesRepository {

    public WrapperPipelinesRepository(String location, Map<String, Object> config) {
        super(location, config);
    }



    @Override
    public Collection<IPipelineDescriptor> getAll() throws PipelinesRepositoryException {
        return getAllWrapped()
                .stream()
                .map((pipeline) -> {
                    if(pipeline == null)
                        return null;

                    pipeline =  new WrapperPipelineDescriptor(pipeline);
                    return pipeline;
                })
                .collect(Collectors.toList());
    }

    @Override
    public IPipelineDescriptor get(String pipelineName) throws PipelinesRepositoryException {
        IPipelineDescriptor pipeline = getWrapped(pipelineName);

        if(pipeline == null)
            return null;

        pipeline =  new WrapperPipelineDescriptor(pipeline);
        return pipeline;
    }

    @Override
    public void update(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        pipeline = new WrapperPipelineDescriptor(pipeline);
        updateWrapped(pipeline);
    }

    @Override
    public void insert(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        pipeline =  new WrapperPipelineDescriptor(pipeline);
        insertWrapped(pipeline);
    }


    protected abstract Collection<IPipelineDescriptor> getAllWrapped() throws PipelinesRepositoryException;
    protected abstract IPipelineDescriptor getWrapped(String pipelineName) throws PipelinesRepositoryException;
    protected abstract void updateWrapped(IPipelineDescriptor pipeline) throws PipelinesRepositoryException;
    protected abstract void insertWrapped(IPipelineDescriptor pipeline) throws PipelinesRepositoryException;

}
