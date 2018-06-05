package pt.isel.ngspipes.dsl_core.descriptors.tool;

import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.GithubToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.LocalToolsRepository;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.ServerToolsRepository;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class ToolsRepositoryFactory {

    @FunctionalInterface
    public interface IToolsRepositoryFactory {

         IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException;

    }



    private static final Collection<IToolsRepositoryFactory> FACTORIES;



    static {
        FACTORIES = new LinkedList<>();

        FACTORIES.add(LocalToolsRepository::create);
        FACTORIES.add(GithubToolsRepository::create);
        FACTORIES.add(ServerToolsRepository::create);
    }



    public static void registerFactory(IToolsRepositoryFactory factory) {
        FACTORIES.add(factory);
    }

    public static void deregisterFactory(IToolsRepositoryFactory factory) {
        FACTORIES.remove(factory);
    }

    public static IToolsRepository create(String location, Map<String, Object> config) throws ToolsRepositoryException {
        try {
            IToolsRepository repository = createRepository(location, config);

            if(repository == null)
                throw new ToolsRepositoryException("Could not find factory for repository with location:" + location);

            return repository;
        } catch (ToolsRepositoryException e) {
            throw new ToolsRepositoryException("Could not create instance of IToolsRepository for location:" + location, e);
        }
    }

    private static IToolsRepository createRepository(String location, Map<String, Object> config) throws ToolsRepositoryException {
        IToolsRepository repository = null;
        ToolsRepositoryException ex = null;

        for(IToolsRepositoryFactory factory : FACTORIES) {
            try {
                repository = factory.create(location, config);
            } catch (ToolsRepositoryException e) {
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
