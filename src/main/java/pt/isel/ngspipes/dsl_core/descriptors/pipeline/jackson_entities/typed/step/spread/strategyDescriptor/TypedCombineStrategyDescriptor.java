package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.strategyDescriptor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public abstract class TypedCombineStrategyDescriptor extends TypedStrategyDescriptor {

    private TypedStrategyDescriptor firstStrategy;
    public TypedStrategyDescriptor getFirstStrategy() { return this.firstStrategy; }
    public void setFirstStrategy(TypedStrategyDescriptor firstStrategy) { this.firstStrategy = firstStrategy; }

    private TypedStrategyDescriptor secondStrategy;
    public TypedStrategyDescriptor getSecondStrategy() { return this.secondStrategy; }
    public void setSecondStrategy(TypedStrategyDescriptor secondStrategy) { this.secondStrategy = secondStrategy; }



    public TypedCombineStrategyDescriptor(TypedStrategyDescriptor firstStrategy, TypedStrategyDescriptor secondStrategy) {
        this.firstStrategy = firstStrategy;
        this.secondStrategy = secondStrategy;
    }

    public TypedCombineStrategyDescriptor() { }

}
