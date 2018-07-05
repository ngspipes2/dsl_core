package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value;

public class TypedParameterValueDescriptor extends TypedValueDescriptor {

    private String parameterName;
    public String getParameterName() { return this.parameterName; }
    public void setParameterName(String parameterName) { this.parameterName = parameterName; }



    public TypedParameterValueDescriptor(String parameterName) {
        this.parameterName = parameterName;
    }

    public TypedParameterValueDescriptor() { }

}
