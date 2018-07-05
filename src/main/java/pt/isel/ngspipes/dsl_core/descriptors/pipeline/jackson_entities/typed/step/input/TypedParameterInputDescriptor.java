package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input;

public class TypedParameterInputDescriptor extends TypedInputDescriptor {

    private String parameterName;
    public String getParameterName() { return this.parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }



    public TypedParameterInputDescriptor(String inputName, String parameterName) {
        super(inputName);
        this.parameterName = parameterName;
    }

    public TypedParameterInputDescriptor() { }

}
