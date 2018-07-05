package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public abstract class TypedInputDescriptor {

    private String inputName;
    public String getInputName() { return this.inputName; }
    public void setInputName(String inputName) { this.inputName = inputName; }



    public TypedInputDescriptor(String inputName) {
        this.inputName = inputName;
    }

    public TypedInputDescriptor() { }

}

