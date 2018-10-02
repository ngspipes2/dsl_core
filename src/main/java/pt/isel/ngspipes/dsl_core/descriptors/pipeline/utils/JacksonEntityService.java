package pt.isel.ngspipes.dsl_core.descriptors.pipeline.utils;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.fileBased.FileBasedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.output.TypedOutputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.parameter.TypedParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.TypedPipelineRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.TypedRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.TypedToolRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value.TypedParameterValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value.TypedSimpleValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.value.TypedValueDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.TypedStepDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec.TypedCommandExecDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec.TypedExecDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec.TypedPipelineExecDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input.TypedChainInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input.TypedInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input.TypedParameterInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.input.TypedSimpleInputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.TypedSpreadDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.spread.strategyDescriptor.*;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.IOutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.OutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.IParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.ParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.*;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.*;
import pt.isel.ngspipes.pipeline_descriptor.step.IStepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.StepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.*;
import pt.isel.ngspipes.pipeline_descriptor.step.input.*;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.ISpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.SpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.*;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class JacksonEntityService {

    public static Collection<TypedPipelineDescriptor> transformToTypedPipelineDescriptor(Collection<IPipelineDescriptor> pipelines) throws PipelinesRepositoryException {
        if(pipelines == null)
            return null;

        Collection<TypedPipelineDescriptor> typedPipelines = new LinkedList<>();

        for(IPipelineDescriptor pipeline : pipelines)
            typedPipelines.add(transformToTypedPipelineDescriptor(pipeline));

        return typedPipelines;
    }

    public static TypedPipelineDescriptor transformToTypedPipelineDescriptor(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        if(pipeline == null)
            return null;

        TypedPipelineDescriptor typedPipeline = new TypedPipelineDescriptor();

        typedPipeline.setName(pipeline.getName());
        typedPipeline.setDescription(pipeline.getDescription());
        typedPipeline.setAuthor(pipeline.getAuthor());
        typedPipeline.setVersion(pipeline.getVersion());
        typedPipeline.setDocumentation(pipeline.getDocumentation());
        typedPipeline.setLogo(pipeline.getLogo());
        typedPipeline.setParameters(transformToTypedParameterDescriptor(pipeline.getParameters()));
        typedPipeline.setOutputs(transformToTypedOutputDescriptor(pipeline.getOutputs()));
        typedPipeline.setRepositories(transformToTypedRepositoryDescriptor(pipeline.getRepositories()));
        typedPipeline.setSteps(transformToTypedStepDescriptor(pipeline.getSteps()));

        return typedPipeline;
    }

    public static Collection<TypedOutputDescriptor> transformToTypedOutputDescriptor(Collection<IOutputDescriptor> outputs) throws PipelinesRepositoryException {
        if(outputs == null)
            return null;

        Collection<TypedOutputDescriptor> typedOutputs = new LinkedList<>();

        for(IOutputDescriptor output : outputs)
            typedOutputs.add(transformToTypedOutputDescriptor(output));

        return typedOutputs;
    }

    public static TypedOutputDescriptor transformToTypedOutputDescriptor(IOutputDescriptor output) throws PipelinesRepositoryException {
        if(output == null)
            return null;

        TypedOutputDescriptor typedOutput = new TypedOutputDescriptor();

        typedOutput.setName(output.getName());
        typedOutput.setStepId(output.getStepId());
        typedOutput.setOutputName(output.getOutputName());

        return typedOutput;
    }

    public static Collection<TypedParameterDescriptor> transformToTypedParameterDescriptor(Collection<IParameterDescriptor> parameters) throws PipelinesRepositoryException {
        if(parameters == null)
            return null;

        Collection<TypedParameterDescriptor> typedParameters = new LinkedList<>();

        for(IParameterDescriptor parameter : parameters)
            typedParameters.add(transformToTypedParameterDescriptor(parameter));

        return typedParameters;
    }

    public static TypedParameterDescriptor transformToTypedParameterDescriptor(IParameterDescriptor parameter) throws PipelinesRepositoryException {
        if(parameter == null)
            return null;

        TypedParameterDescriptor typedParameter = new TypedParameterDescriptor();

        typedParameter.setName(parameter.getName());
        typedParameter.setDefaultValue(parameter.getDefaultValue());

        return typedParameter;
    }

    public static Collection<TypedRepositoryDescriptor> transformToTypedRepositoryDescriptor(Collection<IRepositoryDescriptor> repositories) throws PipelinesRepositoryException {
        if(repositories == null)
            return null;

        Collection<TypedRepositoryDescriptor> typedRepositories = new LinkedList<>();

        for(IRepositoryDescriptor repository : repositories)
            typedRepositories.add(transformToTypedRepositoryDescriptor(repository));

        return typedRepositories;
    }

    public static TypedRepositoryDescriptor transformToTypedRepositoryDescriptor(IRepositoryDescriptor repository) throws PipelinesRepositoryException {
        if(repository == null)
            return null;

        if(repository instanceof IPipelineRepositoryDescriptor)
            return transformToTypedPipelineRepositoryDescriptor((IPipelineRepositoryDescriptor)repository);

        if(repository instanceof IToolRepositoryDescriptor)
            return transformToTypedToolRepositoryDescriptor((IToolRepositoryDescriptor)repository);

        throw new PipelinesRepositoryException("Could not transform IRepositoryDescriptor to TypedRepositoryDescriptor!");
    }

    public static TypedPipelineRepositoryDescriptor transformToTypedPipelineRepositoryDescriptor(IPipelineRepositoryDescriptor repository) throws PipelinesRepositoryException {
        TypedPipelineRepositoryDescriptor typedRepository = new TypedPipelineRepositoryDescriptor();

        typedRepository.setId(repository.getId());
        typedRepository.setLocation(repository.getLocation());
        typedRepository.setConfiguration(transformToTypedConfigDescriptor(repository.getConfiguration()));

        return typedRepository;
    }

    public static TypedToolRepositoryDescriptor transformToTypedToolRepositoryDescriptor(IToolRepositoryDescriptor repository) throws PipelinesRepositoryException {
        TypedToolRepositoryDescriptor typedRepository = new TypedToolRepositoryDescriptor();

        typedRepository.setId(repository.getId());
        typedRepository.setLocation(repository.getLocation());
        typedRepository.setConfiguration(transformToTypedConfigDescriptor(repository.getConfiguration()));

        return typedRepository;
    }

    public static Map<String, TypedValueDescriptor> transformToTypedConfigDescriptor(Map<String, IValueDescriptor> config) throws PipelinesRepositoryException {
        if(config == null)
            return null;

        Map<String, TypedValueDescriptor> typedConfig = new HashMap<>();

        for(String key : config.keySet())
            typedConfig.put(key, transformToTypedValueDescriptor(config.get(key)));

        return typedConfig;
    }

    public static TypedValueDescriptor transformToTypedValueDescriptor(IValueDescriptor value) throws PipelinesRepositoryException {
        if(value == null)
            return null;

        if(value instanceof ISimpleValueDescriptor)
            return new TypedSimpleValueDescriptor(((ISimpleValueDescriptor)value).getValue());

        if(value instanceof IParameterValueDescriptor)
            return new TypedParameterValueDescriptor(((IParameterValueDescriptor)value).getParameterName());

        throw new PipelinesRepositoryException("Could not transform IValueDescriptor to TypedValueDescriptor!");
    }

    public static Collection<TypedStepDescriptor> transformToTypedStepDescriptor(Collection<IStepDescriptor> steps) throws PipelinesRepositoryException {
        if(steps == null)
            return null;

        Collection<TypedStepDescriptor> typedSteps = new LinkedList<>();

        for(IStepDescriptor step : steps)
            typedSteps.add(transformToTypedStepDescriptor(step));

        return typedSteps;
    }

    public static TypedStepDescriptor transformToTypedStepDescriptor(IStepDescriptor step) throws PipelinesRepositoryException {
        if(step == null)
            return null;

        TypedStepDescriptor typedStep = new TypedStepDescriptor();

        typedStep.setId(step.getId());
        typedStep.setExec(transformToTypedExecDescriptor(step.getExec()));
        typedStep.setExecutionContext(transformToTypedValueDescriptor(step.getExecutionContext()));
        typedStep.setInputs(transformToTypedInputDescriptor(step.getInputs()));
        typedStep.setSpread(transformToTypedSpreadDescriptor(step.getSpread()));

        return typedStep;
    }

    public static TypedExecDescriptor transformToTypedExecDescriptor(IExecDescriptor exec) throws PipelinesRepositoryException {
        if(exec == null)
            return null;

        if(exec instanceof ICommandExecDescriptor) {
            ICommandExecDescriptor commandExecDescriptor = (ICommandExecDescriptor) exec;
            return new TypedCommandExecDescriptor(commandExecDescriptor.getRepositoryId(), commandExecDescriptor.getToolName(), commandExecDescriptor.getCommandName());
        }

        if(exec instanceof IPipelineExecDescriptor) {
            IPipelineExecDescriptor pipelineExecDescriptor= (IPipelineExecDescriptor) exec;
            return new TypedPipelineExecDescriptor(pipelineExecDescriptor.getRepositoryId(), pipelineExecDescriptor.getPipelineName());
        }

        throw new PipelinesRepositoryException("Could not transform IExecDescriptor to TypedExecDescriptor!");
    }

    public static Collection<TypedInputDescriptor> transformToTypedInputDescriptor(Collection<IInputDescriptor> inputs) throws PipelinesRepositoryException {
        if(inputs == null)
            return null;

        Collection<TypedInputDescriptor> typedInputs = new LinkedList<>();

        for(IInputDescriptor input : inputs)
            typedInputs.add(transformToTypedInputDescriptor(input));

        return typedInputs;
    }

    public static TypedInputDescriptor transformToTypedInputDescriptor(IInputDescriptor input) throws PipelinesRepositoryException {
        if(input == null)
            return null;

        if(input instanceof ISimpleInputDescriptor)
            return new TypedSimpleInputDescriptor(input.getInputName(), ((ISimpleInputDescriptor) input).getValue());

        if(input instanceof IParameterInputDescriptor)
            return new TypedParameterInputDescriptor(input.getInputName(), ((IParameterInputDescriptor) input).getParameterName());

        if(input instanceof IChainInputDescriptor) {
            IChainInputDescriptor chainInput = (IChainInputDescriptor)input;
            return new TypedChainInputDescriptor(input.getInputName(), chainInput.getStepId(), chainInput.getOutputName());
        }

        throw new PipelinesRepositoryException("Could not transform IInputDescriptor to TypedInputDescriptor!");
    }

    public static TypedSpreadDescriptor transformToTypedSpreadDescriptor(ISpreadDescriptor spread) throws PipelinesRepositoryException {
        if(spread == null)
            return null;

        TypedSpreadDescriptor typedSpread = new TypedSpreadDescriptor();

        typedSpread.setInputsToSpread(spread.getInputsToSpread());
        typedSpread.setStrategy(transformToTypedCombineStrategyDescriptor(spread.getStrategy()));

        return typedSpread;
    }

    private static TypedStrategyDescriptor transformToTypedStrategyDescriptor(IStrategyDescriptor strategy) throws PipelinesRepositoryException {
        if(strategy == null)
            return null;

        if(strategy instanceof ICombineStrategyDescriptor)
            return transformToTypedCombineStrategyDescriptor((ICombineStrategyDescriptor) strategy);

        if(strategy instanceof IInputStrategyDescriptor)
            return new TypedInputStrategyDescriptor(((IInputStrategyDescriptor) strategy).getInputName());

        throw new PipelinesRepositoryException("Could not transform IStrategyDescriptor to TypedStrategyDescriptor!");
    }

    private static TypedCombineStrategyDescriptor transformToTypedCombineStrategyDescriptor(ICombineStrategyDescriptor strategy) throws PipelinesRepositoryException {
        if(strategy == null)
            return null;

        if(strategy instanceof IOneToOneStrategyDescriptor) {
            IOneToOneStrategyDescriptor oneToOneStrategy = (IOneToOneStrategyDescriptor)strategy;
            return new TypedOneToOneStrategyDescriptor(
                    transformToTypedStrategyDescriptor(oneToOneStrategy.getFirstStrategy()),
                    transformToTypedStrategyDescriptor(oneToOneStrategy.getSecondStrategy())
            );
        }

        if(strategy instanceof IOneToManyStrategyDescriptor) {
            IOneToManyStrategyDescriptor oneToManyStrategy = (IOneToManyStrategyDescriptor)strategy;
            return new TypedOneToManyStrategyDescriptor(
                    transformToTypedStrategyDescriptor(oneToManyStrategy.getFirstStrategy()),
                    transformToTypedStrategyDescriptor(oneToManyStrategy.getSecondStrategy())
            );
        }

        throw new PipelinesRepositoryException("Could not transform ICombineStrategyDescriptor to TypedCombineStrategyDescriptor!");
    }


    public static FileBasedPipelineDescriptor transformToFileBasedPipelineDescriptor(IPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        return transformToFileBasedPipelineDescriptor(transformToTypedPipelineDescriptor(pipeline));
    }

    public static FileBasedPipelineDescriptor transformToFileBasedPipelineDescriptor(TypedPipelineDescriptor pipeline) throws PipelinesRepositoryException {
        return new FileBasedPipelineDescriptor(pipeline);
    }


    public static Collection<IPipelineDescriptor> transformToIPipelineDescriptor(Collection<TypedPipelineDescriptor> typedPipelines) throws PipelinesRepositoryException {
        if(typedPipelines == null)
            return null;

        Collection<IPipelineDescriptor> pipelines = new LinkedList<>();

        for(TypedPipelineDescriptor typedPipeline : typedPipelines)
            pipelines.add(transformToIPipelineDescriptor(typedPipeline));

        return pipelines;
    }

    public static IPipelineDescriptor transformToIPipelineDescriptor(TypedPipelineDescriptor typedPipeline) throws PipelinesRepositoryException {
        if(typedPipeline == null)
            return null;

        IPipelineDescriptor pipeline = new PipelineDescriptor();

        pipeline.setName(typedPipeline.getName());
        pipeline.setDescription(typedPipeline.getDescription());
        pipeline.setAuthor(typedPipeline.getAuthor());
        pipeline.setVersion(typedPipeline.getVersion());
        pipeline.setDocumentation(typedPipeline.getDocumentation());
        pipeline.setLogo(typedPipeline.getLogo());
        pipeline.setParameters(transformToIParameterDescriptor(typedPipeline.getParameters()));
        pipeline.setOutputs(transformToIOutputDescriptor(typedPipeline.getOutputs()));
        pipeline.setRepositories(transformToIRepositoryDescriptor(typedPipeline.getRepositories()));
        pipeline.setSteps(transformToIStepDescriptor(typedPipeline.getSteps()));

        return pipeline;
    }

    public static Collection<IOutputDescriptor> transformToIOutputDescriptor(Collection<TypedOutputDescriptor> typedOutputs) throws PipelinesRepositoryException {
        if(typedOutputs == null)
            return null;

        Collection<IOutputDescriptor> outputs = new LinkedList<>();

        for(TypedOutputDescriptor typedOutput : typedOutputs)
            outputs.add(transformToIOutputDescriptor(typedOutput));

        return outputs;
    }

    public static IOutputDescriptor transformToIOutputDescriptor(TypedOutputDescriptor typedOutput) throws PipelinesRepositoryException {
        if(typedOutput == null)
            return null;

        IOutputDescriptor output = new OutputDescriptor();

        output.setName(typedOutput.getName());
        output.setStepId(typedOutput.getStepId());
        output.setOutputName(typedOutput.getOutputName());

        return output;
    }

    public static Collection<IParameterDescriptor> transformToIParameterDescriptor(Collection<TypedParameterDescriptor> typedParameters) throws PipelinesRepositoryException {
        if(typedParameters == null)
            return null;

        Collection<IParameterDescriptor> parameters = new LinkedList<>();

        for(TypedParameterDescriptor typedParameter : typedParameters)
            parameters.add(transformToIParameterDescriptor(typedParameter));

        return parameters;
    }

    public static IParameterDescriptor transformToIParameterDescriptor(TypedParameterDescriptor typedParameter) throws PipelinesRepositoryException {
        if(typedParameter == null)
            return null;

        IParameterDescriptor parameter = new ParameterDescriptor();

        parameter.setName(typedParameter.getName());
        parameter.setDefaultValue(typedParameter.getDefaultValue());

        return parameter;
    }

    public static Collection<IRepositoryDescriptor> transformToIRepositoryDescriptor(Collection<TypedRepositoryDescriptor> typedRepositories) throws PipelinesRepositoryException {
        if(typedRepositories == null)
            return null;

        Collection<IRepositoryDescriptor> repositories = new LinkedList<>();

        for(TypedRepositoryDescriptor typedRepository : typedRepositories)
            repositories.add(transformToIRepositoryDescriptor(typedRepository));

        return repositories;
    }

    public static IRepositoryDescriptor transformToIRepositoryDescriptor(TypedRepositoryDescriptor typedRepository) throws PipelinesRepositoryException {
        if(typedRepository == null)
            return null;

        if(typedRepository instanceof TypedPipelineRepositoryDescriptor)
            return transformToIPipelineRepositoryDescriptor((TypedPipelineRepositoryDescriptor)typedRepository);

        if(typedRepository instanceof TypedToolRepositoryDescriptor)
            return transformToIToolRepositoryDescriptor((TypedToolRepositoryDescriptor)typedRepository);

        throw new PipelinesRepositoryException("Could not transform TypedRepositoryDescriptor to IRepositoryDescriptor!");
    }

    public static IPipelineRepositoryDescriptor transformToIPipelineRepositoryDescriptor(TypedPipelineRepositoryDescriptor typedRepository) throws PipelinesRepositoryException {
        IPipelineRepositoryDescriptor repository = new PipelineRepositoryDescriptor();

        repository.setId(typedRepository.getId());
        repository.setLocation(typedRepository.getLocation());
        repository.setConfiguration(transformToConfigDescriptor(typedRepository.getConfiguration()));

        return repository;
    }

    public static IToolRepositoryDescriptor transformToIToolRepositoryDescriptor(TypedToolRepositoryDescriptor typedRepository) throws PipelinesRepositoryException {
        IToolRepositoryDescriptor repository = new ToolRepositoryDescriptor();

        repository.setId(typedRepository.getId());
        repository.setLocation(typedRepository.getLocation());
        repository.setConfiguration(transformToConfigDescriptor(typedRepository.getConfiguration()));

        return repository;
    }

    public static Map<String, IValueDescriptor> transformToConfigDescriptor(Map<String, TypedValueDescriptor> typedConfig) throws PipelinesRepositoryException {
        if(typedConfig == null)
            return null;

        Map<String, IValueDescriptor> config = new HashMap<>();

        for(String key : typedConfig.keySet())
            config.put(key, transformToIValueDescriptor(typedConfig.get(key)));

        return config;
    }

    public static IValueDescriptor transformToIValueDescriptor(TypedValueDescriptor typedValue) throws PipelinesRepositoryException {
        if(typedValue == null)
            return null;

        if(typedValue instanceof TypedSimpleValueDescriptor)
            return new SimpleValueDescriptor(((TypedSimpleValueDescriptor)typedValue).getValue());

        if(typedValue instanceof TypedParameterValueDescriptor)
            return new ParameterValueDescriptor(((TypedParameterValueDescriptor)typedValue).getParameterName());

        throw new PipelinesRepositoryException("Could not transform TypedValueDescriptor to IValueDescriptor!");
    }

    public static Collection<IStepDescriptor> transformToIStepDescriptor(Collection<TypedStepDescriptor> typedSteps) throws PipelinesRepositoryException {
        if(typedSteps == null)
            return null;

        Collection<IStepDescriptor> steps = new LinkedList<>();

        for(TypedStepDescriptor typedStep : typedSteps)
            steps.add(transformToIStepDescriptor(typedStep));

        return steps;
    }

    public static IStepDescriptor transformToIStepDescriptor(TypedStepDescriptor typedStep) throws PipelinesRepositoryException {
        if(typedStep == null)
            return null;

        IStepDescriptor step = new StepDescriptor();

        step.setId(typedStep.getId());
        step.setExec(transformToIExecDescriptor(typedStep.getExec()));
        step.setExecutionContext(transformToIValueDescriptor(typedStep.getExecutionContext()));
        step.setInputs(transformToIInputDescriptor(typedStep.getInputs()));
        step.setSpread(transformToISpreadDescriptor(typedStep.getSpread()));

        return step;
    }

    public static IExecDescriptor transformToIExecDescriptor(TypedExecDescriptor typedExec) throws PipelinesRepositoryException {
        if(typedExec == null)
            return null;

        if(typedExec instanceof TypedCommandExecDescriptor) {
            TypedCommandExecDescriptor typedCommandExecDescriptor = (TypedCommandExecDescriptor) typedExec;
            return new CommandExecDescriptor(typedCommandExecDescriptor.getRepositoryId(), typedCommandExecDescriptor.getToolName(), typedCommandExecDescriptor.getCommandName());
        }

        if(typedExec instanceof TypedPipelineExecDescriptor) {
            TypedPipelineExecDescriptor typedPipelineExecDescriptor= (TypedPipelineExecDescriptor) typedExec;
            return new PipelineExecDescriptor(typedPipelineExecDescriptor.getRepositoryId(), typedPipelineExecDescriptor.getPipelineName());
        }

        throw new PipelinesRepositoryException("Could not transform TypedExecDescriptor to IExecDescriptor!");
    }

    public static Collection<IInputDescriptor> transformToIInputDescriptor(Collection<TypedInputDescriptor> typedInputs) throws PipelinesRepositoryException {
        if(typedInputs == null)
            return null;

        Collection<IInputDescriptor> inputs = new LinkedList<>();

        for(TypedInputDescriptor input : typedInputs)
            inputs.add(transformToIInputDescriptor(input));

        return inputs;
    }

    public static IInputDescriptor transformToIInputDescriptor(TypedInputDescriptor typedInput) throws PipelinesRepositoryException {
        if(typedInput == null)
            return null;

        if(typedInput instanceof TypedSimpleInputDescriptor)
            return new SimpleInputDescriptor(typedInput.getInputName(), ((TypedSimpleInputDescriptor) typedInput).getValue());

        if(typedInput instanceof TypedParameterInputDescriptor)
            return new ParameterInputDescriptor(typedInput.getInputName(), ((TypedParameterInputDescriptor) typedInput).getParameterName());

        if(typedInput instanceof TypedChainInputDescriptor) {
            TypedChainInputDescriptor typedChainInputDescriptor = (TypedChainInputDescriptor)typedInput;
            return new ChainInputDescriptor(typedChainInputDescriptor.getInputName(), typedChainInputDescriptor.getStepId(), typedChainInputDescriptor.getOutputName());
        }

        throw new PipelinesRepositoryException("Could not transform TypedInputDescriptor to IInputDescriptor!");
    }

    public static ISpreadDescriptor transformToISpreadDescriptor(TypedSpreadDescriptor typedSpread) throws PipelinesRepositoryException {
        if(typedSpread == null)
            return null;

        ISpreadDescriptor spread = new SpreadDescriptor();

        spread.setInputsToSpread(typedSpread.getInputsToSpread());
        spread.setStrategy(transformToICombineStrategyDescriptor(typedSpread.getStrategy()));

        return spread;
    }

    private static IStrategyDescriptor transformToIStrategyDescriptor(TypedStrategyDescriptor typedStrategy) throws PipelinesRepositoryException {
        if(typedStrategy == null)
            return null;

        if(typedStrategy instanceof TypedCombineStrategyDescriptor)
            return transformToICombineStrategyDescriptor((TypedCombineStrategyDescriptor) typedStrategy);

        if(typedStrategy instanceof TypedInputStrategyDescriptor)
            return new InputStrategyDescriptor(((TypedInputStrategyDescriptor) typedStrategy).getInputName());

        throw new PipelinesRepositoryException("Could not transform TypedStrategyDescriptor to IStrategyDescriptor!");
    }

    private static ICombineStrategyDescriptor transformToICombineStrategyDescriptor(TypedCombineStrategyDescriptor typedStrategy) throws PipelinesRepositoryException {
        if(typedStrategy == null)
            return null;

        if(typedStrategy instanceof TypedOneToOneStrategyDescriptor) {
            TypedOneToOneStrategyDescriptor typedOneToOneStrategy = (TypedOneToOneStrategyDescriptor)typedStrategy;
            return new OneToOneStrategyDescriptor(
                    transformToIStrategyDescriptor(typedOneToOneStrategy.getFirstStrategy()),
                    transformToIStrategyDescriptor(typedOneToOneStrategy.getSecondStrategy())
            );
        }

        if(typedStrategy instanceof TypedOneToManyStrategyDescriptor) {
            TypedOneToManyStrategyDescriptor typedOneToManyStrategy = (TypedOneToManyStrategyDescriptor)typedStrategy;
            return new OneToManyStrategyDescriptor(
                    transformToIStrategyDescriptor(typedOneToManyStrategy.getFirstStrategy()),
                    transformToIStrategyDescriptor(typedOneToManyStrategy.getSecondStrategy())
            );
        }

        throw new PipelinesRepositoryException("Could not transform TypedCombineStrategyDescriptor to ICombineStrategyDescriptor!");
    }

}
