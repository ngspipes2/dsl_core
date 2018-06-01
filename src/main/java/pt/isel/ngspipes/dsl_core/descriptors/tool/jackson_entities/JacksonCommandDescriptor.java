package pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.CommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IOutputDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;

import java.util.Collection;

public class JacksonCommandDescriptor implements ICommandDescriptor {

    private ICommandDescriptor descriptorSource;

    public JacksonCommandDescriptor(ICommandDescriptor descriptorSource) {
        this.descriptorSource = descriptorSource;
    }

    public JacksonCommandDescriptor() { this(new CommandDescriptor()); }

    @Override
    public String getName() { return descriptorSource.getName(); }
    @Override
    public void setName(String name) { descriptorSource.setName(name); }

    @Override
    public String getCommand() { return descriptorSource.getCommand(); }
    @Override
    public void setCommand(String command) { descriptorSource.setCommand(command); }

    @Override
    public String getDescription() { return descriptorSource.getDescription(); }
    @Override
    public void setDescription(String description) { descriptorSource.setDescription(description); }

    @JsonGetter("recommended_mem")
    @Override
    public int getRecommendedMemory() { return descriptorSource.getRecommendedMemory(); }
    @JsonSetter("recommended_mem")
    @Override
    public void setRecommendedMemory(int recommendedMemory) { descriptorSource.setRecommendedMemory(recommendedMemory); }

    @JsonGetter("recommended_disk")
    @Override
    public int getRecommendedDisk() { return descriptorSource.getRecommendedDisk(); }
    @JsonSetter("recommended_disk")
    @Override
    public void setRecommendedDisk(int recommendedDisk) { descriptorSource.setRecommendedDisk(recommendedDisk); }

    @JsonGetter("recommended_cpu")
    @Override
    public int getRecommendedCpu() { return descriptorSource.getRecommendedCpu(); }
    @JsonSetter("recommended_cpu")
    @Override
    public void setRecommendedCpu(int recommendedCpu) {
        descriptorSource.setRecommendedCpu(recommendedCpu);
    }

    @Override
    public Collection<IParameterDescriptor> getParameters() { return descriptorSource.getParameters(); }
    @Override
    public void setParameters(Collection<IParameterDescriptor> parameters) { descriptorSource.setParameters(parameters); }

    @Override
    public Collection<IOutputDescriptor> getOutputs() { return descriptorSource.getOutputs(); }
    @Override
    public void setOutputs(Collection<IOutputDescriptor> outputs) { descriptorSource.setOutputs(outputs); }

}
