package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.wrapper;

import pt.isel.ngspipes.tool_descriptor.implementations.CommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;

import java.util.stream.Collectors;

public class WrapperCommandDescriptor extends CommandDescriptor {

    private ICommandDescriptor source;



    public WrapperCommandDescriptor(ICommandDescriptor source) {
        super(
            source.getName(),
            source.getDescription(),
            source.getRecommendedMemory(),
            source.getRecommendedDisk(),
            source.getRecommendedCpu(),
            source.getParameters() == null ?
                null : source.getParameters().stream().map(WrapperParameterDescriptor::new).collect(Collectors.toList()),
            source.getOutputs() == null ?
                null : source.getOutputs().stream().map(WrapperOutputDescriptor::new).collect(Collectors.toList()),
            source.getCommand()
        );

        this.source = source;
    }

}
