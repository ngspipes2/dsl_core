package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread.strategyDescriptor;

import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IInputStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.InputStrategyDescriptor;

public class WrapperInputStrategyDescriptor extends InputStrategyDescriptor {

    private IInputStrategyDescriptor source;



    public WrapperInputStrategyDescriptor(IInputStrategyDescriptor source) {
        super(
            source.getInputName()
        );

        this.source = source;
    }

}
