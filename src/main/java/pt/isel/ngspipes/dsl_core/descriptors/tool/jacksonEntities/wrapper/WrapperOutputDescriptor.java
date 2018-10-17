package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.wrapper;

import pt.isel.ngspipes.tool_descriptor.implementations.OutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IOutputDescriptor;

public class WrapperOutputDescriptor extends OutputDescriptor {

    private IOutputDescriptor source;



    public WrapperOutputDescriptor(IOutputDescriptor source) {
        super(
            source.getType(),
            source.getName(),
            source.getDescription(),
            source.getValue()
        );

        this.source = source;
    }

}
