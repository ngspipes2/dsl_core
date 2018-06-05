package pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.util.Collection;

public class FileBasedToolDescriptor implements IToolDescriptor {

    private IToolDescriptor descriptorSource;

    public FileBasedToolDescriptor(IToolDescriptor descriptor) {
        descriptorSource = descriptor;
    }

    public FileBasedToolDescriptor() { this(new ToolDescriptor()); }

    @Override
    public String getName() { return descriptorSource.getName(); }
    @Override
    public void setName(String name) { descriptorSource.setName(name); }

    @Override
    public String getAuthor() { return descriptorSource.getAuthor(); }
    @Override
    public void setAuthor(String author) { descriptorSource.setAuthor(author); }

    @Override
    public String getDescription() { return descriptorSource.getDescription(); }
    @Override
    public void setDescription(String description) { descriptorSource.setDescription(description); }

    @Override
    public String getVersion() { return descriptorSource.getVersion(); }
    @Override
    public void setVersion(String version) { descriptorSource.setVersion(version); }

    @Override
    public Collection<String> getDocumentation() { return descriptorSource.getDocumentation(); }
    @Override
    public void setDocumentation(Collection<String> documentation) { descriptorSource.setDocumentation(documentation); }

    @Override
    public Collection<ICommandDescriptor> getCommands() { return descriptorSource.getCommands(); }
    @Override
    public void setCommands(Collection<ICommandDescriptor> commands) { descriptorSource.setCommands(commands); }

    @JsonIgnore
    @Override
    public byte[] getLogo() { return descriptorSource.getLogo(); }
    @Override
    public void setLogo(byte[] logo) { descriptorSource.setLogo(logo); }

    @JsonIgnore
    @Override
    public Collection<IExecutionContextDescriptor> getExecutionContexts() { return descriptorSource.getExecutionContexts(); }
    @Override
    public void setExecutionContexts(Collection<IExecutionContextDescriptor> executionContexts) {
        descriptorSource.setExecutionContexts(executionContexts);
    }
}
