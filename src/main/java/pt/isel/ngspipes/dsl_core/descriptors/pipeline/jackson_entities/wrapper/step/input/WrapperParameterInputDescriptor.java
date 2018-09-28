package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.input;

import pt.isel.ngspipes.pipeline_descriptor.step.input.IParameterInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.ParameterInputDescriptor;

public class WrapperParameterInputDescriptor extends ParameterInputDescriptor {

    private IParameterInputDescriptor source;



    public WrapperParameterInputDescriptor(IParameterInputDescriptor source) {
        super(
            source.getInputName(),
            source.getParameterName()
        );

        this.source = source;
    }

}
