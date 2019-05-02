package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.output;

import pt.isel.ngspipes.pipeline_descriptor.output.IOutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.OutputDescriptor;

public class WrapperOutputDescriptor extends OutputDescriptor {

    private IOutputDescriptor source;



    public WrapperOutputDescriptor(IOutputDescriptor source) {
        super(
            source.getName(),
            source.getStepId(),
            source.getOutputName()
        );

        this.source = source;
    }

}
