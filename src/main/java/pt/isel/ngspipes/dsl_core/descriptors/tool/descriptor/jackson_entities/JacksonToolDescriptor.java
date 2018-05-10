package pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IExecutionContextDescriptor;

import java.util.Collection;

public class JacksonToolDescriptor extends ToolDescriptor {

    @JsonIgnore
    @Override
    public String getLogo() { return super.getLogo(); }

    @JsonIgnore
    @Override
    public Collection<IExecutionContextDescriptor> getExecutionContexts() { return super.getExecutionContexts(); }
}
