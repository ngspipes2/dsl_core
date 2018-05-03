package pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.jackson_entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;

import java.util.Collection;

public class JacksonParameterDescriptor extends ParameterDescriptor {

    @JsonGetter("dependent_values")
    @Override
    public String[] getDependentValues() { return super.getDependentValues(); }

    @JsonSetter("dependent_values")
    @Override
    public void setDependentValues(String[] dependentValues) { super.setDependentValues(dependentValues); }

    @JsonGetter("sub_parameters")
    @Override
    public Collection<IParameterDescriptor> getSubParameters() { return super.getSubParameters(); }

    @JsonSetter("sub_parameters")
    @Override
    public void setSubParameters(Collection<IParameterDescriptor> subParameters) { super.setSubParameters(subParameters); }

}
