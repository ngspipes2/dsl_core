package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value.TypedValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec.TypedExecDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input.TypedInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.TypedSpreadDescriptor;

import java.util.Collection;

public class TypedStepDescriptor {

    private String id;
    public String getId() { return this.id; }
    public void setId(String id){ this.id = id; }

    private TypedExecDescriptor exec;
    public TypedExecDescriptor getExec() { return this.exec; }
    public void setExec(TypedExecDescriptor exec) { this.exec = exec; }

    private TypedValueDescriptor executionContext;
    public TypedValueDescriptor getExecutionContext() { return this.executionContext; }
    public void setExecutionContext(TypedValueDescriptor executionContext) { this.executionContext = executionContext; }

    private Collection<TypedInputDescriptor> inputs;
    public Collection<TypedInputDescriptor> getInputs() { return this.inputs; }
    public void setInputs(Collection<TypedInputDescriptor> inputs) { this.inputs = inputs; }

    private TypedSpreadDescriptor spread;
    public TypedSpreadDescriptor getSpread() { return this.spread; }
    public void setSpread(TypedSpreadDescriptor spread) { this.spread = spread; }



    public TypedStepDescriptor(String id, TypedExecDescriptor exec, TypedValueDescriptor executionContext, Collection<TypedInputDescriptor> inputs, TypedSpreadDescriptor spread) {
        this.id = id;
        this.exec = exec;
        this.executionContext = executionContext;
        this.inputs = inputs;
        this.spread = spread;
    }

    public TypedStepDescriptor() { }

}
