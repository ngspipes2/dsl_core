package pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.wrapper;

import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.util.stream.Collectors;

public class WrapperToolDescriptor extends ToolDescriptor {

    private IToolDescriptor source;



    public WrapperToolDescriptor(IToolDescriptor source) {
        super(
            source.getName(),
            source.getAuthor(),
            source.getDescription(),
            source.getVersion(),
            source.getDocumentation(),
            source.getCommands() == null ?
                null : source.getCommands().stream().map(WrapperCommandDescriptor::new).collect(Collectors.toList()),
            source.getLogo(),
            source.getExecutionContexts() == null ?
                null : source.getExecutionContexts().stream().map(WrapperExecutionContextDescriptor::new).collect(Collectors.toList())
        );

        this.source = source;
    }
    
}
