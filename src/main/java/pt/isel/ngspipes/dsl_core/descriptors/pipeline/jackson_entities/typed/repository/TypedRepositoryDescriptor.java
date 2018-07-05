package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value.TypedValueDescriptor;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public abstract class TypedRepositoryDescriptor {

    private String id;
    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    private String location;
    public String getLocation() { return this.location; }
    public void setLocation(String location) { this.location = location; }

    private Map<String, TypedValueDescriptor> configuration;
    public Map<String, TypedValueDescriptor> getConfiguration() { return this.configuration; }
    public void setConfiguration(Map<String, TypedValueDescriptor> configuration) { this.configuration = configuration; }



    public TypedRepositoryDescriptor(String id, String location, Map<String, TypedValueDescriptor> configuration) {
        this.id = id;
        this.location = location;
        this.configuration = configuration;
    }

    public TypedRepositoryDescriptor() { }

}
