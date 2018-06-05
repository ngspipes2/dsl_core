package pt.isel.ngspipes.dsl_core.descriptors.tool.utils;

import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.FileBasedCommandDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.FileBasedParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.tool.jackson_entities.FileBasedToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.ICommandDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IParameterDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.util.Collection;
import java.util.LinkedList;

public class JacksonEntityService {

    public static FileBasedToolDescriptor transformToFileBasedToolDescriptor(IToolDescriptor tool) {
        Collection<ICommandDescriptor> commands = transformToFileBasedCommandsDescriptors(tool.getCommands());
        tool.setCommands(commands);
        return new FileBasedToolDescriptor(tool);
    }

    public static Collection<ICommandDescriptor> transformToFileBasedCommandsDescriptors(Collection<ICommandDescriptor> commands) {
        Collection<ICommandDescriptor> jacksonCommands = new LinkedList<>();

        if (commands == null)
            return jacksonCommands;

        for (ICommandDescriptor command : commands)
            jacksonCommands.add(transformToFileBasedCommandDescriptor(command));

        return jacksonCommands;
    }

    public static FileBasedCommandDescriptor transformToFileBasedCommandDescriptor(ICommandDescriptor command) {
        Collection<IParameterDescriptor> parameters = transformToFileBasedParametersDescriptors(command.getParameters());
        command.setParameters(parameters);
        return new FileBasedCommandDescriptor(command);
    }

    public static Collection<IParameterDescriptor> transformToFileBasedParametersDescriptors(Collection<IParameterDescriptor> parameters) {
        Collection<IParameterDescriptor> jacksonParameters = new LinkedList<>();

        if (parameters == null)
            return jacksonParameters;

        for (IParameterDescriptor parameter : parameters)
            jacksonParameters.add(transformToFileBasedParameterDescriptor(parameter));

        return jacksonParameters;
    }

    public static FileBasedParameterDescriptor transformToFileBasedParameterDescriptor(IParameterDescriptor parameter) {
        return new FileBasedParameterDescriptor(parameter);
    }

}
