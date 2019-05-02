package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.wrapper;

import pt.isel.ngspipes.tool_descriptor.implementations.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;

import java.util.stream.Collectors;

public class WrapperParameterDescriptor extends ParameterDescriptor {

    private IParameterDescriptor source;



    public WrapperParameterDescriptor(IParameterDescriptor source) {
        super(
            source.getName(),
            source.getDescription(),
            source.getValues(),
            source.getType(),
            source.isRequired(),
            source.getPrefix(),
            source.getSuffix(),
            source.getSeparator(),
            source.getDepends(),
            source.getDependentValues(),
            source.getSubParameters() == null ?
                null : source.getSubParameters().stream().map(WrapperParameterDescriptor::new).collect(Collectors.toList())
        );

        this.source = source;
    }

}
