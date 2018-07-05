package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value;

public class TypedSimpleValueDescriptor extends TypedValueDescriptor {

    private Object value;
    public Object getValue() { return this.value; }
    public void setValue(Object value) { this.value = value; }



    public TypedSimpleValueDescriptor(Object value) {
        this.value = value;
    }

    public TypedSimpleValueDescriptor() { }
}
