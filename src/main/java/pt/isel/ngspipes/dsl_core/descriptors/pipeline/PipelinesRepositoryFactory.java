package pt.isel.ngspipes.dsl_core.descriptors.pipeline;

import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class PipelinesRepositoryFactory {

    @FunctionalInterface
    public interface IPipelinesRepositoryFactory {

        IPipelinesRepository create(String location, Map<String, Object> config);

    }



    private static final Collection<IPipelinesRepositoryFactory> FACTORIES = new LinkedList<>();



    public static void registerFactory(IPipelinesRepositoryFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IPipelinesRepositoryFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        IPipelinesRepository repository;

        for(IPipelinesRepositoryFactory factory : FACTORIES) {
            repository = factory.create(location, config);

            if(repository != null)
                return repository;
        }

        throw new PipelinesRepositoryException("Could not find factory to create PipelineRepository for location " + location + "!");
    }

}
