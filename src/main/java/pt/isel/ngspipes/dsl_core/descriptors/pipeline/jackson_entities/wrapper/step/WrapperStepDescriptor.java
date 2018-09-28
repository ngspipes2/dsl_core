package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.repository.value.WrapperParameterValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.repository.value.WrapperSimpleValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.exec.WrapperCommandExecDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.exec.WrapperPipelineExecDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.input.WrapperChainInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.input.WrapperParameterInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.input.WrapperSimpleInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.wrapper.step.spread.WrapperSpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IParameterValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.ISimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.IStepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.StepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.ICommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IPipelineExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IChainInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.IParameterInputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.ISimpleInputDescriptor;

import java.util.stream.Collectors;

public class WrapperStepDescriptor extends StepDescriptor {

    private static IExecDescriptor wrapExec(IExecDescriptor exec) {
        if(exec instanceof ICommandExecDescriptor)
            return new WrapperCommandExecDescriptor((ICommandExecDescriptor) exec);
        else if(exec instanceof IPipelineExecDescriptor)
            return new WrapperPipelineExecDescriptor((IPipelineExecDescriptor) exec);

        throw new IllegalArgumentException("Unknown exec descriptor " + exec.getClass().getName());
    }

    private static IValueDescriptor wrapValue(IValueDescriptor value) {
        if(value instanceof IParameterValueDescriptor)
            value = new WrapperParameterValueDescriptor((IParameterValueDescriptor) value);
        else if(value instanceof ISimpleValueDescriptor)
            value = new WrapperSimpleValueDescriptor((ISimpleValueDescriptor) value);
        else
            throw new IllegalArgumentException("Unknown value descriptor " + value.getClass().getName());

        return value;
    }

    private static IInputDescriptor wrapInput(IInputDescriptor input) {
        if(input instanceof IChainInputDescriptor)
            return new WrapperChainInputDescriptor((IChainInputDescriptor) input);
        else if(input instanceof IParameterInputDescriptor)
            return new WrapperParameterInputDescriptor((IParameterInputDescriptor) input);
        else if(input instanceof ISimpleInputDescriptor)
            return new WrapperSimpleInputDescriptor((ISimpleInputDescriptor) input);

        throw new IllegalArgumentException("Unknown input descriptor " + input.getClass().getName());
    }



    private IStepDescriptor source;



    public WrapperStepDescriptor(IStepDescriptor source) {
        super(
            source.getId(),
            source.getExec() == null ?
                null : wrapExec(source.getExec()),
            source.getExecutionContext() == null ?
                null : wrapValue(source.getExecutionContext()),
            source.getInputs() == null ?
                null : source.getInputs().stream().map(WrapperStepDescriptor::wrapInput).collect(Collectors.toList()),
            source.getSpread() == null ?
                null : new WrapperSpreadDescriptor(source.getSpread())
        );

        this.source = source;
    }

}
