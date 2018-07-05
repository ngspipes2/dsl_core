package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value.TypedValueDescriptor;

import java.util.Map;

public class TypedToolRepositoryDescriptor extends TypedRepositoryDescriptor {

    public TypedToolRepositoryDescriptor(String id, String location, Map<String, TypedValueDescriptor> configuration) {
        super(id, location, configuration);
    }

    public TypedToolRepositoryDescriptor() {
        super();
    }

}
