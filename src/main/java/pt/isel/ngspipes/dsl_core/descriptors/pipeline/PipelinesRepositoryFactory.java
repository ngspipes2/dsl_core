package pt.isel.ngspipes.dsl_core.descriptors.pipeline;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.GithubPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.LocalPipelinesRepository;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.repository.ServerPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class PipelinesRepositoryFactory {

    @FunctionalInterface
    public interface IPipelinesRepositoryFactory {

        IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException;

    }



    private static final Collection<IPipelinesRepositoryFactory> FACTORIES;



    static {
        FACTORIES = new LinkedList<>();

        FACTORIES.add(LocalPipelinesRepository::create);
        FACTORIES.add(GithubPipelinesRepository::create);
        FACTORIES.add(ServerPipelinesRepository::create);
    }



    public static void registerFactory(IPipelinesRepositoryFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IPipelinesRepositoryFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IPipelinesRepository create(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        try {
            IPipelinesRepository repository = createRepository(location, config);

            if(repository == null)
                throw new PipelinesRepositoryException("Could not find factory for repository with location:" + location);

            return repository;
        } catch (PipelinesRepositoryException e) {
            throw new PipelinesRepositoryException("Could not create instance of IPipelinesRepository for location:" + location, e);
        }
    }

    private static IPipelinesRepository createRepository(String location, Map<String, Object> config) throws PipelinesRepositoryException {
        IPipelinesRepository repository = null;
        PipelinesRepositoryException ex = null;

        for(PipelinesRepositoryFactory.IPipelinesRepositoryFactory factory : FACTORIES) {
            try {
                repository = factory.create(location, config);
            } catch (PipelinesRepositoryException e) {
                if(ex == null)
                    ex = e;
            }

            if(repository != null)
                return repository;
        }

        if(ex != null)
            throw ex;

        return null;
    }


}
