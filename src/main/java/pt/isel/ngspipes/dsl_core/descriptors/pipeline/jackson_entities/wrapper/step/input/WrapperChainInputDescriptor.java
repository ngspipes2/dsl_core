package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.input;

import pt.isel.ngspipes.pipeline_descriptor.step.input.ChainInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IChainInputDescriptor;

public class WrapperChainInputDescriptor extends ChainInputDescriptor {

    private IChainInputDescriptor source;



    public WrapperChainInputDescriptor(IChainInputDescriptor source) {
        super(
            source.getInputName(),
            source.getStepId(),
            source.getOutputName()
        );

        this.source = source;
    }

}
