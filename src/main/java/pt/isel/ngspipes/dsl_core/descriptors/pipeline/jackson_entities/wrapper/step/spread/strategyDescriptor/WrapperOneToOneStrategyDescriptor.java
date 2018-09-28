package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread.strategyDescriptor;

import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.*;

public class WrapperOneToOneStrategyDescriptor extends OneToOneStrategyDescriptor {

    private static IStrategyDescriptor wrapStrategy(IStrategyDescriptor strategy) {
        if(strategy instanceof IOneToOneStrategyDescriptor)
            return new WrapperOneToOneStrategyDescriptor((IOneToOneStrategyDescriptor) strategy);
        else if(strategy instanceof IOneToManyStrategyDescriptor)
            return new WrapperOneToManyStrategyDescriptor((IOneToManyStrategyDescriptor) strategy);
        else if(strategy instanceof IInputStrategyDescriptor)
            return new WrapperInputStrategyDescriptor((IInputStrategyDescriptor) strategy);

        throw new IllegalArgumentException("Unknown strategy descriptor " + strategy.getClass().getName());
    }



    private IOneToOneStrategyDescriptor source;



    public WrapperOneToOneStrategyDescriptor(IOneToOneStrategyDescriptor source) {
        super(
            source.getFirstStrategy() == null ?
                null : wrapStrategy(source.getFirstStrategy()),
            source.getSecondStrategy() == null ?
                null : wrapStrategy(source.getSecondStrategy())
        );

        this.source = source;
    }

}
