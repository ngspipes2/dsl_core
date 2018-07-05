package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.output;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public class TypedOutputDescriptor {

    private String name;
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    private String stepId;
    public String getStepId() { return this.stepId; }
    public void setStepId(String stepId) { this.stepId = stepId; }

    private String outputName;
    public String getOutputName() { return this.outputName; }
    public void setOutputName(String outputName) { this.outputName = outputName; }



    public TypedOutputDescriptor(String name, String stepId, String outputName) {
        this.name = name;
        this.stepId = stepId;
        this.outputName = outputName;
    }

    public TypedOutputDescriptor() { }

}
