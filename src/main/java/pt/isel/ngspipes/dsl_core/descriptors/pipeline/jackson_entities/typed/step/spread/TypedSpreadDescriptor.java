package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.strategyDescriptor.TypedCombineStrategyDescriptor;

import java.util.Collection;

public class TypedSpreadDescriptor {

    private Collection<String> inputsToSpread;
    public Collection<String> getInputsToSpread() { return this.inputsToSpread; }
    public void setInputsToSpread(Collection<String> inputsToSpread) { this.inputsToSpread = inputsToSpread; }

    private TypedCombineStrategyDescriptor strategy;
    public TypedCombineStrategyDescriptor getStrategy() { return this.strategy; }
    public void setStrategy(TypedCombineStrategyDescriptor strategy) { this.strategy = strategy; }



    public TypedSpreadDescriptor(Collection<String> inputsToSpread, TypedCombineStrategyDescriptor strategy) {
        this.inputsToSpread = inputsToSpread;
        this.strategy = strategy;
    }

    public TypedSpreadDescriptor() { }

}
