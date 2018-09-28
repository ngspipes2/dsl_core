package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread.strategyDescriptor.WrapperOneToManyStrategyDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread.strategyDescriptor.WrapperOneToOneStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.ISpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.SpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.ICombineStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IOneToManyStrategyDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.IOneToOneStrategyDescriptor;

public class WrapperSpreadDescriptor extends SpreadDescriptor {

    private static ICombineStrategyDescriptor wrapStrategy(ICombineStrategyDescriptor strategy) {
        if(strategy instanceof IOneToOneStrategyDescriptor)
            return new WrapperOneToOneStrategyDescriptor((IOneToOneStrategyDescriptor) strategy);
        else if(strategy instanceof IOneToManyStrategyDescriptor)
            return new WrapperOneToManyStrategyDescriptor((IOneToManyStrategyDescriptor) strategy);

        throw new IllegalArgumentException("Unknown strategy descriptor " + strategy.getClass().getName());
    }



    private ISpreadDescriptor source;



    public WrapperSpreadDescriptor(ISpreadDescriptor source) {
        super(
            source.getInputsToSpread(),
            source.getStrategy() == null ?
                null : wrapStrategy(source.getStrategy())
        );

        this.source = source;
    }

}
