package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.parameter;

import pt.isel.ngspipes.pipeline_descriptor.parameter.IParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.ParameterDescriptor;

public class WrapperParameterDescriptor extends ParameterDescriptor {

    private IParameterDescriptor source;



    public WrapperParameterDescriptor(IParameterDescriptor source) {
        super(
            source.getName(),
            source.getDefaultValue()
        );

        this.source = source;
    }

}
