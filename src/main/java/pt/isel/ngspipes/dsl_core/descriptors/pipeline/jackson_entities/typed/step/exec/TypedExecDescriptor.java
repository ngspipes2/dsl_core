package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public abstract class TypedExecDescriptor {

    private String repositoryId;
    public String getRepositoryId() { return this.repositoryId; }
    public void setRepositoryId(String repositoryId) { this.repositoryId = repositoryId; }



    public TypedExecDescriptor(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public TypedExecDescriptor() { }

}
