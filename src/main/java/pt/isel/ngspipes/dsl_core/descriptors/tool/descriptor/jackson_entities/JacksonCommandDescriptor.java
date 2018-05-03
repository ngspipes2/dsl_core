package pt.isel.ngspipes.dsl_core.descriptors.tool.descriptor.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import pt.isel.ngspipes.tool_descriptor.implementations.CommandDescriptor;

public class JacksonCommandDescriptor extends CommandDescriptor {

    @JsonSetter("recommended_mem")
    @Override
    public void setRecommendedMemory(int recommendedMemory) {
        super.setRecommendedMemory(recommendedMemory);
    }
    @JsonGetter("recommended_mem")
    @Override
    public int getRecommendedMemory() { return super.getRecommendedMemory(); }

    @JsonSetter("recommended_disk")
    @Override
    public void setRecommendedDisk(int recommendedDisk) { super.setRecommendedDisk(recommendedDisk); }
    @JsonGetter("recommended_disk")
    @Override
    public int getRecommendedDisk() { return super.getRecommendedDisk(); }


    @JsonSetter("recommended_cpu")
    @Override
    public void setRecommendedCpu(int recommendedCpu) {
        super.setRecommendedCpu(recommendedCpu);
    }
    @JsonGetter("recommended_cpu")
    @Override
    public int getRecommendedCpu() { return super.getRecommendedCpu(); }

}
