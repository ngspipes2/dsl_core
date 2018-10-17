package tools;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.ToolMapper;
import pt.isel.ngspipes.tool_descriptor.implementations.*;
import pt.isel.ngspipes.tool_descriptor.interfaces.*;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class ToolsMapperTest {

    private static IToolDescriptor createDummyTool() {
        IToolDescriptor tool = new ToolDescriptor();

        tool.setName(UUID.randomUUID().toString());

        tool.setCommands(new LinkedList<>());
        tool.getCommands().add(createDummyCommand());

        tool.setExecutionContexts(new LinkedList<>());
        tool.getExecutionContexts().add(createDummyExecutionContext());

        return tool;
    }

    private static ICommandDescriptor createDummyCommand() {
        ICommandDescriptor command = new CommandDescriptor();

        command.setName(UUID.randomUUID().toString());

        command.setParameters(new LinkedList<>());
        command.getParameters().add(createDummyParameter());

        command.setOutputs(new LinkedList<>());
        command.getOutputs().add(createDummyOutput());

        return command;
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

    private static IExecutionContextDescriptor createDummyExecutionContext() {
        IExecutionContextDescriptor executionContext = new ExecutionContextDescriptor();

        executionContext.setName(UUID.randomUUID().toString());

        return executionContext;
    }



    @Test
    public void simpleTest() throws IOException {
        ObjectMapper mapper = ToolMapper.getToolsMapper(new JsonFactory());
        IToolDescriptor tool = createDummyTool();

        String toolStr = mapper.writeValueAsString(tool);
        IToolDescriptor toolObj = mapper.readValue(toolStr, IToolDescriptor.class);

        assertEquals(tool, toolObj);
    }

    private void assertEquals(IToolDescriptor tool, IToolDescriptor toolObj) {
        TestCase.assertEquals(tool.getName(), toolObj.getName());
        assertEqualsContexts(tool.getExecutionContexts(), toolObj.getExecutionContexts());
        assertEqualsCommands(tool.getCommands(), toolObj.getCommands());
    }

    private void assertEqualsCommands(Collection<ICommandDescriptor> commands, Collection<ICommandDescriptor> commandsObj) {
        TestCase.assertEquals(commands.size(), commandsObj.size());
        assertEquals(commands.stream().findFirst().get(), commandsObj.stream().findFirst().get());
    }

    private void assertEquals(ICommandDescriptor command, ICommandDescriptor commandObj) {
        TestCase.assertEquals(command.getName(), commandObj.getName());
        assertEqualsParameters(command.getParameters(), commandObj.getParameters());
        assertEqualsOutputs(command.getOutputs(), commandObj.getOutputs());
    }

    private void assertEqualsParameters(Collection<IParameterDescriptor> parameters, Collection<IParameterDescriptor> parametersObj) {
        TestCase.assertEquals(parameters.size(), parametersObj.size());
        assertEquals(parameters.stream().findFirst().get(), parametersObj.stream().findFirst().get());
    }

    private void assertEquals(IParameterDescriptor parameter, IParameterDescriptor parameterObj) {
        TestCase.assertEquals(parameter.getName(), parameterObj.getName());
    }

    private void assertEqualsOutputs(Collection<IOutputDescriptor> outputs, Collection<IOutputDescriptor> outputsObj) {
        TestCase.assertEquals(outputs.size(), outputsObj.size());
        assertEquals(outputs.stream().findFirst().get(), outputsObj.stream().findFirst().get());
    }

    private void assertEquals(IOutputDescriptor output, IOutputDescriptor outputObj) {
        TestCase.assertEquals(output.getName(), outputObj.getName());
    }

    private void assertEqualsContexts(Collection<IExecutionContextDescriptor> executionContexts, Collection<IExecutionContextDescriptor> executionContextsObj) {
        TestCase.assertEquals(executionContexts.size(), executionContextsObj.size());
        assertEquals(executionContexts.stream().findFirst().get(), executionContextsObj.stream().findFirst().get());
    }

    private void assertEquals(IExecutionContextDescriptor executionContext, IExecutionContextDescriptor executionContextObj) {
        TestCase.assertEquals(executionContext.getName(), executionContextObj.getName());
    }

}
