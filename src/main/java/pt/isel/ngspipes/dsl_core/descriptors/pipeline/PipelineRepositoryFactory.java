package pt.isel.ngspipes.dsl_core.descriptors.pipeline;

import pt.isel.ngspipes.pipeline_repository.IPipelineRepository;
import pt.isel.ngspipes.pipeline_repository.PipelineRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class PipelineRepositoryFactory {

    @FunctionalInterface
    public interface IPipelineRepositoryFactory {

        IPipelineRepository create(String location, Map<String, Object> config) throws PipelineRepositoryException;

    }



    private static final Collection<IPipelineRepositoryFactory> FACTORIES = new LinkedList<>();



    public static void registerFactory(IPipelineRepositoryFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IPipelineRepositoryFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IPipelineRepository create(String location, Map<String, Object> config) throws PipelineRepositoryException {
        IPipelineRepository repository;

        for(IPipelineRepositoryFactory factory : FACTORIES) {
            repository = factory.create(location, config);

            if(repository != null)
                return repository;
        }

        throw new PipelineRepositoryException("Could not find factory to create PipelineRepository for location " + location + "!");
    }

}
