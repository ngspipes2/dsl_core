package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.input;

import pt.isel.ngspipes.pipeline_descriptor.step.input.ISimpleInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.SimpleInputDescriptor;

public class WrapperSimpleInputDescriptor extends SimpleInputDescriptor {

    private ISimpleInputDescriptor source;



    public WrapperSimpleInputDescriptor(ISimpleInputDescriptor source) {
        super(
            source.getInputName(),
            source.getValue()
        );

        this.source = source;
    }

}
