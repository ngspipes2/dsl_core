package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.JacksonToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.util.Collection;
import java.util.LinkedList;

public class JacksonEntityService {

    public static JacksonToolDescriptor transformToJacksonToolDescriptor(IToolDescriptor tool) {
        Collection<ICommandDescriptor> commands = transformToJacksonCommandsDescriptors(tool.getCommands());
        tool.setCommands(commands);
        return new JacksonToolDescriptor(tool);
    }

    public static Collection<ICommandDescriptor> transformToJacksonCommandsDescriptors(Collection<ICommandDescriptor> commands) {
        Collection<ICommandDescriptor> jacksonCommands = new LinkedList<>();

        if (commands == null)
            return jacksonCommands;

        for (ICommandDescriptor command : commands)
            jacksonCommands.add(transformToJacksonCommandDescriptor(command));

        return jacksonCommands;
    }

    public static JacksonCommandDescriptor transformToJacksonCommandDescriptor(ICommandDescriptor command) {
        Collection<IParameterDescriptor> parameters = transformToJacksonParametersDescriptors(command.getParameters());
        command.setParameters(parameters);
        return new JacksonCommandDescriptor(command);
    }

    public static Collection<IParameterDescriptor> transformToJacksonParametersDescriptors(Collection<IParameterDescriptor> parameters) {
        Collection<IParameterDescriptor> jacksonParameters = new LinkedList<>();

        if (parameters == null)
            return jacksonParameters;

        for (IParameterDescriptor parameter : parameters)
            jacksonParameters.add(transformToJacksonParameterDescriptor(parameter));

        return jacksonParameters;
    }

    public static JacksonParameterDescriptor transformToJacksonParameterDescriptor(IParameterDescriptor parameter) {
        return new JacksonParameterDescriptor(parameter);
    }

}
