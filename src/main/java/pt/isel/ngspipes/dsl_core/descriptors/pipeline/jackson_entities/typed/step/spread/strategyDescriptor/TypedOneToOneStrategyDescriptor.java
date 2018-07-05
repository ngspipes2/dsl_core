package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.strategyDescriptor;

public class TypedOneToOneStrategyDescriptor extends TypedCombineStrategyDescriptor {

    public TypedOneToOneStrategyDescriptor(TypedStrategyDescriptor firstStrategy, TypedStrategyDescriptor secondStrategy) {
        super(firstStrategy, secondStrategy);
    }

    public TypedOneToOneStrategyDescriptor() { }

}
