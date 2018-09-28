package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.repository.value;

import pt.isel.ngspipes.pipeline_descriptor.repository.value.IParameterValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.ParameterValueDescriptor;

public class WrapperParameterValueDescriptor extends ParameterValueDescriptor {

    private IParameterValueDescriptor source;



    public WrapperParameterValueDescriptor(IParameterValueDescriptor source) {
        super(
            source.getParameterName()
        );

        this.source = source;
    }

}
