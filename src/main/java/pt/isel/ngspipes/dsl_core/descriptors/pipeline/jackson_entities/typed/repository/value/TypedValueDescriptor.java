package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "__class")
public abstract class TypedValueDescriptor { }
