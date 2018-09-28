package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.repository.value;

import pt.isel.ngspipes.pipeline_descriptor.repository.value.ISimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.SimpleValueDescriptor;

public class WrapperSimpleValueDescriptor extends SimpleValueDescriptor {

    private ISimpleValueDescriptor source;



    public WrapperSimpleValueDescriptor(ISimpleValueDescriptor source) {
        super(
            source.getValue()
        );

        this.source = source;
    }

}
