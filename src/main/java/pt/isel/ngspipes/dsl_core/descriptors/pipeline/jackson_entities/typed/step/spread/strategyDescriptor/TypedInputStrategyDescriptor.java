package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.strategyDescriptor;

public class TypedInputStrategyDescriptor extends TypedStrategyDescriptor {

    private String inputName;
    public String getInputName() { return this.inputName; }
    public void setInputName(String inputName) { this.inputName = inputName; }



    public TypedInputStrategyDescriptor(String inputName) {
        this.inputName = inputName;
    }

    public TypedInputStrategyDescriptor() { }

}
