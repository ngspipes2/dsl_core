package pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.tool.ParameterDescriptor;

public class JacksonParameterDescriptor extends ParameterDescriptor {

    @JsonGetter("dependent_value")
    @Override
    public String[] getDependentValues() { return super.getDependentValues(); }

    @JsonSetter
    @Override
    public void setDependentValues(String[] dependentValues) { super.setDependentValues(dependentValues); }

}
