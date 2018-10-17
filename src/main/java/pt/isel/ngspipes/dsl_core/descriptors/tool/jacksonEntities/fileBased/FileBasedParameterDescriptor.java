package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.fileBased;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;

import java.util.Collection;

public class FileBasedParameterDescriptor extends ParameterDescriptor {

    @JsonGetter("dependent_values")
    @Override
    public Collection<String> getDependentValues() { return super.getDependentValues(); }
    @JsonSetter("dependent_values")
    @Override
    public void setDependentValues(Collection<String> dependentValues) { super.setDependentValues(dependentValues); }

    @JsonGetter("sub_parameters")
    @Override
    public Collection<IParameterDescriptor> getSubParameters() { return super.getSubParameters(); }
    @JsonSetter("sub_parameters")
    @Override
    public void setSubParameters(Collection<IParameterDescriptor> subParameters) { super.setSubParameters(subParameters); }



    public FileBasedParameterDescriptor(IParameterDescriptor source) {
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
                source.getSubParameters()
        );
    }

    public FileBasedParameterDescriptor() { this(new ParameterDescriptor()); }

}
