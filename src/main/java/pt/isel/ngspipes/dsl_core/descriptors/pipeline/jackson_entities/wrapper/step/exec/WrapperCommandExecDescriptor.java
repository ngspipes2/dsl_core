package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.exec;

import pt.isel.ngspipes.pipeline_descriptor.step.exec.CommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.ICommandExecDescriptor;

public class WrapperCommandExecDescriptor extends CommandExecDescriptor {

    private ICommandExecDescriptor source;



    public WrapperCommandExecDescriptor(ICommandExecDescriptor source) {
        super(
            source.getRepositoryId(),
            source.getToolName(),
            source.getCommandName()
        );

        this.source = source;
    }

}
