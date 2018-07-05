package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input;

public class TypedSimpleInputDescriptor extends TypedInputDescriptor {

    private Object value;
    public Object getValue() { return this.value; }
    public void setValue(Object value) { this.value = value; }



    public TypedSimpleInputDescriptor(String inputName, Object value) {
        super(inputName);
        this.value = value;
    }

    public TypedSimpleInputDescriptor() { }

}
