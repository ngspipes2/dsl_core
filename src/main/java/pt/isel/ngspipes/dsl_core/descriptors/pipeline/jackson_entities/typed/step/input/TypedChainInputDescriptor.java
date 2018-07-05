package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input;

public class TypedChainInputDescriptor extends TypedInputDescriptor {

    private String stepId;
    public String getStepId() { return this.stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }

    private String outputName;
    public String getOutputName() { return this.outputName; }
    public void setOutputName(String outputName) { this.outputName = outputName; }



    public TypedChainInputDescriptor(String inputName, String stepId, String outputName) {
        super(inputName);
        this.stepId = stepId;
        this.outputName = outputName;
    }

    public TypedChainInputDescriptor() { }

}
