package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread.strategyDescriptor;

import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.*;

public class WrapperOneToManyStrategyDescriptor extends OneToManyStrategyDescriptor {

    private static IStrategyDescriptor wrapStrategy(IStrategyDescriptor strategy) {
        if(strategy instanceof IOneToOneStrategyDescriptor)
            return new WrapperOneToOneStrategyDescriptor((IOneToOneStrategyDescriptor) strategy);
        else if(strategy instanceof IOneToManyStrategyDescriptor)
            return new WrapperOneToManyStrategyDescriptor((IOneToManyStrategyDescriptor) strategy);
        else if(strategy instanceof IInputStrategyDescriptor)
            return new WrapperInputStrategyDescriptor((IInputStrategyDescriptor) strategy);

        throw new IllegalArgumentException("Unknown strategy descriptor " + strategy.getClass().getName());
    }



    private IOneToManyStrategyDescriptor source;



    public WrapperOneToManyStrategyDescriptor(IOneToManyStrategyDescriptor source) {
        super(
            source.getFirstStrategy() == null ?
                null : wrapStrategy(source.getFirstStrategy()),
            source.getSecondStrategy() == null ?
                null : wrapStrategy(source.getSecondStrategy())
        );

        this.source = source;
    }

}
