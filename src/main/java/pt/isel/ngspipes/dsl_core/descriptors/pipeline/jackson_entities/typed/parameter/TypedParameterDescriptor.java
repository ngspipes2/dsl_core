package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.parameter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public class TypedParameterDescriptor {

    private String name;
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    private Object defaultValue;
    public Object getDefaultValue() { return this.defaultValue; }
    public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }



    public TypedParameterDescriptor(String name, Object defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public TypedParameterDescriptor() { }

}
