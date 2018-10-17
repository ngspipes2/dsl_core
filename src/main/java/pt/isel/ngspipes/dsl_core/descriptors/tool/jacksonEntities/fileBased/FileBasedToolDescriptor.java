package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.fileBased;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.util.Collection;
import java.util.stream.Collectors;

public class FileBasedToolDescriptor extends ToolDescriptor {

    @JsonIgnore
    @Override
    public byte[] getLogo() { return super.getLogo(); }

    @JsonIgnore
    @Override
    public Collection<IExecutionContextDescriptor> getExecutionContexts() { return super.getExecutionContexts(); }



    public FileBasedToolDescriptor(IToolDescriptor source) {
        super(
            source.getName(),
            source.getAuthor(),
            source.getDescription(),
            source.getVersion(),
            source.getDocumentation(),
            source.getCommands() == null ?
                    null : source.getCommands().stream().map(FileBasedCommandDescriptor::new).collect(Collectors.toList()),
            source.getLogo(),
            source.getExecutionContexts()
        );
    }

    public FileBasedToolDescriptor() { this(new ToolDescriptor()); }

}
