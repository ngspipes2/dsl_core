package pt.isel.ngspipes.dsl_core.descriptors.pipeline;

import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.PipelineRepositoryException;

import java.util.Collection;
import java.util.LinkedList;

public class PipelineDescriptorFactory {

    @FunctionalInterface
    public interface IPipelineDescriptorFactory {

        IPipelineDescriptor create(String description, String type) throws PipelineRepositoryException;

    }



    private static final Collection<IPipelineDescriptorFactory> FACTORIES = new LinkedList<>();



    public static void registerFactory(IPipelineDescriptorFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IPipelineDescriptorFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IPipelineDescriptor create(String description, String type) throws PipelineRepositoryException {
        IPipelineDescriptor pipeline;

        for(IPipelineDescriptorFactory factory : FACTORIES) {
            pipeline = factory.create(description, type);

            if(pipeline != null)
                return pipeline;
        }

        throw new PipelineRepositoryException("Could not find factory to create PipelineDescriptor for type " + type + "!");
    }

}
