package pt.isel.ngspipes.dsl_core.dsl_parser.descriptors.tool;

import interfaces.IToolsRepository;
import utils.ToolRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class ToolsRepositoryFactory {

    @FunctionalInterface
    public interface IToolsRepositoryFactory {

         IToolsRepository create(String location, Map<String, Object> config);

    }



    private static final Collection<IToolsRepositoryFactory> FACTORIES = new LinkedList<>();



    public static void registerFactory(IToolsRepositoryFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IToolsRepositoryFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolRepositoryException {
        IToolsRepository repository;

        for(IToolsRepositoryFactory factory : FACTORIES){
            repository = factory.create(location, config);

            if(repository != null)
                return repository;
        }

        throw new ToolRepositoryException("Could not find factory to create ToolsRepository for location " + location + "!");
    }

}
