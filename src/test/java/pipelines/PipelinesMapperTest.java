package pipelines;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import junit.framework.TestCase;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.PipelineMapper;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.IOutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.OutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.IParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.ParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.*;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.ISimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.IValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.value.SimpleValueDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.IStepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.StepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.CommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.ICommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.input.*;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.ISpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.SpreadDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.spread.strategyDescriptor.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class PipelinesMapperTest {

    private static IPipelineDescriptor createDummyPipeline() {
        IPipelineDescriptor pipeline = new PipelineDescriptor();

        pipeline.setName(UUID.randomUUID().toString());

        pipeline.setParameters(new LinkedList<>());
        pipeline.getParameters().add(createDummyParameter());

        pipeline.setOutputs(new LinkedList<>());
        pipeline.getOutputs().add(createDummyOutput());

        pipeline.setRepositories(new LinkedList<>());
        pipeline.getRepositories().add(createDummyToolsRepository());
        pipeline.getRepositories().add(createDummyPipelinesRepository());

        pipeline.setSteps(new LinkedList<>());
        pipeline.getSteps().add(createDummyStep());

        return pipeline;
    }

    private static IParameterDescriptor createDummyParameter() {
        IParameterDescriptor parameter = new ParameterDescriptor();

        parameter.setName(UUID.randomUUID().toString());

        return parameter;
    }

    private static IOutputDescriptor createDummyOutput() {
        IOutputDescriptor output = new OutputDescriptor();

        output.setName(UUID.randomUUID().toString());

        return output;
    }

    private static IToolRepositoryDescriptor createDummyToolsRepository() {
        IToolRepositoryDescriptor repository = new ToolRepositoryDescriptor();

        repository.setLocation(UUID.randomUUID().toString());
        repository.setConfiguration(new HashMap<>());

        return repository;
    }

    private static IPipelineRepositoryDescriptor createDummyPipelinesRepository() {
        IPipelineRepositoryDescriptor repository = new PipelineRepositoryDescriptor();

        repository.setLocation(UUID.randomUUID().toString());
        repository.setConfiguration(new HashMap<>());

        return repository;
    }

    private static IStepDescriptor createDummyStep() {
        IStepDescriptor step = new StepDescriptor();

        step.setId(UUID.randomUUID().toString());
        step.setExec(createDummyExec());
        step.setExecutionContext(createDummyValue());

        step.setInputs(new LinkedList<>());
        step.getInputs().add(createDummySimpleInput());
        step.getInputs().add(createDummyChainInput());
        step.getInputs().add(createDummyParameterInput());

        step.setSpread(createDummySpread());

        return step;
    }

    private static IExecDescriptor createDummyExec() {
        ICommandExecDescriptor exec = new CommandExecDescriptor();

        exec.setRepositoryId(UUID.randomUUID().toString());
        exec.setToolName(UUID.randomUUID().toString());
        exec.setCommandName(UUID.randomUUID().toString());

        return exec;
    }

    private static IValueDescriptor createDummyValue() {
        ISimpleValueDescriptor value = new SimpleValueDescriptor();

        value.setValue(UUID.randomUUID().toString());

        return value;
    }

    private static ISimpleInputDescriptor createDummySimpleInput() {
        ISimpleInputDescriptor input = new SimpleInputDescriptor();

        input.setInputName(UUID.randomUUID().toString());

        return input;
    }

    private static IChainInputDescriptor createDummyChainInput() {
        IChainInputDescriptor input = new ChainInputDescriptor();

        input.setInputName(UUID.randomUUID().toString());

        return input;
    }

    private static IParameterInputDescriptor createDummyParameterInput() {
        IParameterInputDescriptor input = new ParameterInputDescriptor();

        input.setInputName(UUID.randomUUID().toString());

        return input;
    }

    private static ISpreadDescriptor createDummySpread() {
        ISpreadDescriptor spread = new SpreadDescriptor();

        spread.setInputsToSpread(new LinkedList<>());
        spread.getInputsToSpread().add(UUID.randomUUID().toString());
        spread.setStrategy(createDummyCombineStrategy());

        return spread;
    }

    private static ICombineStrategyDescriptor createDummyCombineStrategy() {
        return createDummyOneToOneStrategy();
    }

    private static IOneToOneStrategyDescriptor createDummyOneToOneStrategy() {
        IOneToOneStrategyDescriptor strategy = new OneToOneStrategyDescriptor();

        strategy.setFirstStrategy(createDummyInputStrategy());
        strategy.setSecondStrategy(createDummyInputStrategy());

        return strategy;
    }

    private static IOneToManyStrategyDescriptor createDummyOneToManyStrategy() {
        IOneToManyStrategyDescriptor strategy = new OneToManyStrategyDescriptor();

        strategy.setFirstStrategy(createDummyInputStrategy());
        strategy.setSecondStrategy(createDummyInputStrategy());

        return strategy;
    }

    private static IInputStrategyDescriptor createDummyInputStrategy() {
        IInputStrategyDescriptor strategy = new InputStrategyDescriptor();

        strategy.setInputName(UUID.randomUUID().toString());

        return strategy;
    }



    @Test
    public void simpleTest() throws IOException {
        ObjectMapper mapper = PipelineMapper.getPipelinesMapper(new YAMLFactory());
        IPipelineDescriptor pipeline= createDummyPipeline();

        String pipelineStr = mapper.writeValueAsString(pipeline);
        IPipelineDescriptor pipelineObj = mapper.readValue(pipelineStr, IPipelineDescriptor.class);

        assertEquals(pipeline, pipelineObj);
    }

    private static void assertEquals(IPipelineDescriptor pipeline, IPipelineDescriptor pipelineObj) {
        TestCase.assertEquals(pipeline.getName(), pipelineObj.getName());

        assertEqualsParameters(pipeline.getParameters(), pipelineObj.getParameters());
        assertEqualsOutputs(pipeline.getOutputs(), pipelineObj.getOutputs());
        assertEqualsRepositories(pipeline.getRepositories(), pipelineObj.getRepositories());
        assertEqualsSteps(pipeline.getSteps(), pipelineObj.getSteps());
    }

    private static void assertEqualsParameters(Collection<IParameterDescriptor> parameters, Collection<IParameterDescriptor> parametersObj) {
        TestCase.assertEquals(parameters.size(), parametersObj.size());
        assertEquals(parameters.stream().findFirst().get(), parametersObj.stream().findFirst().get());
    }

    private static void assertEquals(IParameterDescriptor parameter, IParameterDescriptor parameterObj) {
        TestCase.assertEquals(parameter.getName(), parameterObj.getName());
    }

    private static void assertEqualsOutputs(Collection<IOutputDescriptor> outputs, Collection<IOutputDescriptor> outputsObj) {
        TestCase.assertEquals(outputs.size(), outputsObj.size());
        assertEquals(outputs.stream().findFirst().get(), outputsObj.stream().findFirst().get());
    }

    private static void assertEquals(IOutputDescriptor output, IOutputDescriptor outputObj) {
        TestCase.assertEquals(output.getName(), outputObj.getName());
    }

    private static void assertEqualsRepositories(Collection<IRepositoryDescriptor> repositories, Collection<IRepositoryDescriptor> repositoriesObj) {
        TestCase.assertEquals(repositories.size(), repositoriesObj.size());
        assertEquals(repositories.stream().skip(0).findFirst().get(), repositoriesObj.stream().skip(0).findFirst().get());
        assertEquals(repositories.stream().skip(1).findFirst().get(), repositoriesObj.stream().skip(1).findFirst().get());
    }

    private static void assertEquals(IRepositoryDescriptor repository, IRepositoryDescriptor repositoryObj) {
        TestCase.assertSame(repository.getClass(), repositoryObj.getClass());
        TestCase.assertEquals(repository.getId(), repositoryObj.getId());
        TestCase.assertEquals(repository.getLocation(), repositoryObj.getLocation());
    }

    private static void assertEqualsSteps(Collection<IStepDescriptor> steps, Collection<IStepDescriptor> stepsObj) {
        TestCase.assertEquals(steps.size(), stepsObj.size());
        assertEquals(steps.stream().findFirst().get(), stepsObj.stream().findFirst().get());
    }

    private static void assertEquals(IStepDescriptor step, IStepDescriptor stepObj) {
        TestCase.assertEquals(step.getId(), stepObj.getId());
        assertEquals(step.getExec(), stepObj.getExec());
        assertEquals(step.getExecutionContext(), stepObj.getExecutionContext());
        assertEqualsInputs(step.getInputs(), stepObj.getInputs());
        assertEquals(step.getSpread(), stepObj.getSpread());
    }

    private static void assertEquals(IExecDescriptor exec, IExecDescriptor execObj) {
        TestCase.assertSame(exec.getClass(), execObj.getClass());
        TestCase.assertEquals(exec.getRepositoryId(), execObj.getRepositoryId());
    }

    private static void assertEquals(IValueDescriptor value, IValueDescriptor valueObj) {
        TestCase.assertSame(value.getClass(), valueObj.getClass());
    }

    private static void assertEqualsInputs(Collection<IInputDescriptor> inputs, Collection<IInputDescriptor> inputsObj) {
        TestCase.assertEquals(inputs.size(), inputsObj.size());

        assertEquals(inputs.stream().skip(0).findFirst().get(), inputsObj.stream().skip(0).findFirst().get());
        assertEquals(inputs.stream().skip(1).findFirst().get(), inputsObj.stream().skip(1).findFirst().get());
        assertEquals(inputs.stream().skip(2).findFirst().get(), inputsObj.stream().skip(2).findFirst().get());
    }

    private static void assertEquals(IInputDescriptor input, IInputDescriptor inputObj) {
        TestCase.assertSame(input.getClass(), inputObj.getClass());
        TestCase.assertEquals(input.getInputName(), inputObj.getInputName());
    }

    private static void assertEquals(ISpreadDescriptor spread, ISpreadDescriptor spreadObj) {
        TestCase.assertEquals(spread.getInputsToSpread().size(), spreadObj.getInputsToSpread().size());
        TestCase.assertEquals(spread.getStrategy().getFirstStrategy().getClass(), spreadObj.getStrategy().getFirstStrategy().getClass());
        TestCase.assertEquals(spread.getStrategy().getSecondStrategy().getClass(), spreadObj.getStrategy().getSecondStrategy().getClass());
    }

}
