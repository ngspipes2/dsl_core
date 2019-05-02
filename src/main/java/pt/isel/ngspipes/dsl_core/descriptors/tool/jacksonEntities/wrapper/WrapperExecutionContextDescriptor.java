package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.wrapper;

import pt.isel.ngspipes.tool_descriptor.implementations.ExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;

public class WrapperExecutionContextDescriptor extends ExecutionContextDescriptor {

    private IExecutionContextDescriptor source;



    public WrapperExecutionContextDescriptor(IExecutionContextDescriptor source) {
        super(
            source.getName(),
            source.getContext(),
            source.getConfig()
        );

        this.source = source;
    }

}
