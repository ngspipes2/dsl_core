package pt.isel.ngspipes.dsl_core.descriptors.tool.jacksonEntities.fileBased;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.CommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;

import java.util.stream.Collectors;

public class FileBasedCommandDescriptor extends CommandDescriptor {

    @JsonGetter("recommended_mem")
    @Override
    public int getRecommendedMemory() { return super.getRecommendedMemory(); }
    @JsonSetter("recommended_mem")
    @Override
    public void setRecommendedMemory(int recommendedMemory) { super.setRecommendedMemory(recommendedMemory); }

    @JsonGetter("recommended_disk")
    @Override
    public int getRecommendedDisk() { return super.getRecommendedDisk(); }
    @JsonSetter("recommended_disk")
    @Override
    public void setRecommendedDisk(int recommendedDisk) { super.setRecommendedDisk(recommendedDisk); }

    @JsonGetter("recommended_cpu")
    @Override
    public int getRecommendedCpu() { return super.getRecommendedCpu(); }
    @JsonSetter("recommended_cpu")
    @Override
    public void setRecommendedCpu(int recommendedCpu) {
        super.setRecommendedCpu(recommendedCpu);
    }



    public FileBasedCommandDescriptor(ICommandDescriptor source) {
        super(
                source.getName(),
                source.getDescription(),
                source.getRecommendedMemory(),
                source.getRecommendedDisk(),
                source.getRecommendedCpu(),
                source.getParameters() == null ?
                        null : source.getParameters().stream().map(FileBasedParameterDescriptor::new).collect(Collectors.toList()),
                source.getOutputs(),
                source.getCommand()
        );
    }

    public FileBasedCommandDescriptor() { this(new CommandDescriptor()); }

}
