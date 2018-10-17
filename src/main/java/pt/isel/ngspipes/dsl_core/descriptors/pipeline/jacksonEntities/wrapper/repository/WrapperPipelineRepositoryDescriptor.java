package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.repository;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.repository.value.WrapperParameterValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.repository.value.WrapperSimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IPipelineRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.PipelineRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IParameterValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.ISimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IValueDescriptor;

import java.util.HashMap;
import java.util.Map;

public class WrapperPipelineRepositoryDescriptor extends PipelineRepositoryDescriptor {

    private static Map<String, IValueDescriptor> wrapConfig(Map<String, IValueDescriptor> config) {
        Map<String, IValueDescriptor> configuration = new HashMap<>();

        IValueDescriptor value;
        for(String key : config.keySet()) {
            value = config.get(key);

            if(value instanceof IParameterValueDescriptor)
                value = new WrapperParameterValueDescriptor((IParameterValueDescriptor) value);
            else if(value instanceof ISimpleValueDescriptor)
                value = new WrapperSimpleValueDescriptor((ISimpleValueDescriptor) value);
            else
                throw new IllegalArgumentException("Unknown value descriptor " + value.getClass().getName());

            configuration.put(key, value);
        }

        return configuration;

    }



    private IPipelineRepositoryDescriptor source;



    public WrapperPipelineRepositoryDescriptor(IPipelineRepositoryDescriptor source) {
        super(
            source.getId(),
            source.getLocation(),
            source.getConfiguration() == null ?
                null : wrapConfig(source.getConfiguration())
        );

        this.source = source;
    }

}
