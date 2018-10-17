package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.step.exec;

import pt.isel.ngspipes.pipeline_descriptor.step.exec.IPipelineExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.PipelineExecDescriptor;

public class WrapperPipelineExecDescriptor extends PipelineExecDescriptor {

    private IPipelineExecDescriptor source;



    public WrapperPipelineExecDescriptor(IPipelineExecDescriptor source) {
        super(
            source.getRepositoryId(),
            source.getPipelineName()
        );

        this.source = source;
    }

}
