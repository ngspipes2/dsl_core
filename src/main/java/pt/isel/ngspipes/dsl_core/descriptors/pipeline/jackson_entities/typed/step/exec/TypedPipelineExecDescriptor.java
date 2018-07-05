package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec;

public class TypedPipelineExecDescriptor extends TypedExecDescriptor {

    private String pipelineName;
    public String getPipelineName() { return this.pipelineName; }
    public void setPipelineName(String pipelineName) { this.pipelineName = pipelineName; }



    public TypedPipelineExecDescriptor(String repositoryId, String pipelineName) {
        super(repositoryId);
        this.pipelineName = pipelineName;
    }

    public TypedPipelineExecDescriptor() { }

}
