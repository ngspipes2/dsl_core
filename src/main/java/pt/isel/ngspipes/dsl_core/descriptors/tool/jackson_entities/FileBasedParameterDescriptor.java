package pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.ParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;

import java.util.Collection;

public class FileBasedParameterDescriptor implements IParameterDescriptor {

    private IParameterDescriptor descriptorSource;


    public FileBasedParameterDescriptor(IParameterDescriptor descriptorSource) {
        this.descriptorSource = descriptorSource;
    }

    public FileBasedParameterDescriptor() { this(new ParameterDescriptor()); }


    @Override
    public String getName() { return descriptorSource.getName(); }
    @Override
    public void setName(String name) { descriptorSource.setName(name); }

    @Override
    public String getDescription() { return descriptorSource.getDescription(); }
    @Override
    public void setDescription(String description) { descriptorSource.setDescription(description); }


    @Override
    public Collection<String> getValues() { return descriptorSource.getValues(); }
    @Override
    public void setValues(Collection<String> values) { descriptorSource.setValues(values); }

    @Override
    public String getType() { return descriptorSource.getType(); }
    @Override
    public void setType(String type) { descriptorSource.setType(type); }

    @Override
    public boolean isRequired() { return descriptorSource.isRequired(); }

    @Override
    public void setRequired(boolean required) { descriptorSource.setRequired(required); }

    @Override
    public String getPrefix() { return descriptorSource.getPrefix(); }
    @Override
    public void setPrefix(String prefix) { descriptorSource.setPrefix(prefix); }

    @Override
    public String getSuffix() { return descriptorSource.getSuffix(); }
    @Override
    public void setSuffix(String suffix) { descriptorSource.setSuffix(suffix); }

    @Override
    public String getSeparator() { return descriptorSource.getSeparator(); }
    @Override
    public void setSeparator(String separator) { descriptorSource.setSeparator(separator); }

    @Override
    public String getDepends() { return descriptorSource.getDepends(); }
    @Override
    public void setDepends(String depends) { descriptorSource.setDepends(depends); }

    @JsonGetter("dependent_values")
    @Override
    public Collection<String> getDependentValues() { return descriptorSource.getDependentValues(); }
    @JsonSetter("dependent_values")
    @Override
    public void setDependentValues(Collection<String> dependentValues) { descriptorSource.setDependentValues(dependentValues); }

    @JsonGetter("sub_parameters")
    @Override
    public Collection<IParameterDescriptor> getSubParameters() { return descriptorSource.getSubParameters(); }
    @JsonSetter("sub_parameters")
    @Override
    public void setSubParameters(Collection<IParameterDescriptor> subParameters) { descriptorSource.setSubParameters(subParameters); }

}
