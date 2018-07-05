package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.strategyDescriptor;

public class TypedOneToManyStrategyDescriptor extends TypedCombineStrategyDescriptor {

    public TypedOneToManyStrategyDescriptor(TypedStrategyDescriptor firstStrategy, TypedStrategyDescriptor secondStrategy) {
        super(firstStrategy, secondStrategy);
    }

    public TypedOneToManyStrategyDescriptor() { }

}
