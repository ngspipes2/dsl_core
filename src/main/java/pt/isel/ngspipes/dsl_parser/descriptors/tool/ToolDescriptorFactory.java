package pt.isel.ngspipes.dsl_core.descriptors.tool;

import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import utils.ToolRepositoryException;

import java.util.Collection;
import java.util.LinkedList;

public class ToolDescriptorFactory {

    @FunctionalInterface
    public interface IToolDescriptorFactory {

        IToolDescriptor create(String description, String type) throws ToolRepositoryException;

    }



    private static final Collection<IToolDescriptorFactory> FACTORIES = new LinkedList<>();



    public static void registerFactory(IToolDescriptorFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IToolDescriptorFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IToolDescriptor create(String description, String type) throws ToolRepositoryException {
        IToolDescriptor tool;

        for(IToolDescriptorFactory factory : FACTORIES) {
            tool = factory.create(description, type);

            if(tool != null)
                return tool;
        }

        throw new ToolRepositoryException("Could not find factory to create ToolDescriptor for type " + type + "!");
    }

}
