package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.fileBased;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;

public class FileBasedPipelineDescriptor extends PipelineDescriptor {

    @JsonIgnore
    @Override
    public byte[] getLogo() {
        return super.getLogo();
    }



    public FileBasedPipelineDescriptor(IPipelineDescriptor source) {
        super(
                source.getName(),
                source.getDescription(),
                source.getAuthor(),
                source.getVersion(),
                source.getDocumentation(),
                source.getLogo(),
                source.getParameters(),
                source.getOutputs(),
                source.getRepositories(),
                source.getSteps()
        );
    }

    public FileBasedPipelineDescriptor() {
        this(new PipelineDescriptor());
    }

}
