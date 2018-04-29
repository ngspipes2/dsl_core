package pt.isel.ngspipes.dsl_core.descriptors.tool;

import interfaces.IToolsRepository;
import utils.ToolRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class ToolRepositoryFactory {

    @FunctionalInterface
    public interface IToolRepositoryFactory {

         IToolsRepository create(String location, Map<String, Object> config) throws ToolRepositoryException;

    }



    private static final Collection<IToolRepositoryFactory> FACTORIES = new LinkedList<>();



    public static void registerFactory(IToolRepositoryFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IToolRepositoryFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolRepositoryException {
        IToolsRepository repository;

        for(IToolRepositoryFactory factory : FACTORIES){
            repository = factory.create(location, config);

            if(repository != null)
                return repository;
        }

        throw new ToolRepositoryException("Could not find factory to create ToolsRepository for location " + location + "!");
    }

}
