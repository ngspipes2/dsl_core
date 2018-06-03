package tools;

import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ngspipes.dsl_core.descriptors.tool.repository.CacheToolsRepository;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;
import pt.isel.ngspipes.tool_repository.interfaces.IToolsRepository;
import utils.ToolsRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CacheRepositoryTest {

    private static class DummyToolsRepository implements IToolsRepository {

        private Map<String, IToolDescriptor> toolsByName;



        public DummyToolsRepository(boolean empty) {
            toolsByName = new HashMap<>();

            if(!empty) {
                IToolDescriptor tool = new ToolDescriptor();
                tool.setName("Trimmomatic");
                toolsByName.put(tool.getName(), tool);

                tool = new ToolDescriptor();
                tool.setName("Velvet");
                toolsByName.put(tool.getName(), tool);

                tool = new ToolDescriptor();
                tool.setName("Blast");
                toolsByName.put(tool.getName(), tool);
            }
        }



        @Override
        public String getLocation() throws ToolsRepositoryException {
            return null;
        }

        @Override
        public Map<String, Object> getConfig() throws ToolsRepositoryException {
            return null;
        }

        @Override
        public Collection<IToolDescriptor> getAll() throws ToolsRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }
            return toolsByName.values();
        }

        @Override
        public IToolDescriptor get(String toolName) throws ToolsRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }
            return toolsByName.get(toolName);
        }

        @Override
        public void insert(IToolDescriptor tool) throws ToolsRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }

            if(toolsByName.containsKey(tool.getName()))
                throw new ToolsRepositoryException("There is already a tool with name:" + tool.getName());

            toolsByName.put(tool.getName(), tool);
        }

        @Override
        public void update(IToolDescriptor tool) throws ToolsRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }

            if(!toolsByName.containsKey(tool.getName()))
                throw new ToolsRepositoryException("There is not tool with name:" + tool.getName());

            toolsByName.put(tool.getName(), tool);
        }

        @Override
        public void delete(String toolName) throws ToolsRepositoryException {
            try { Thread.sleep(5*1000); } catch (Exception e) { }

            toolsByName.remove(toolName);
        }
    }


    private static CacheToolsRepository loadedRepository;



    @BeforeClass
    public static void init() throws ToolsRepositoryException {
        loadedRepository = new CacheToolsRepository(new DummyToolsRepository(false));
        loadedRepository.getAll();
    }


    @Test
    public void insertNonExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.insertNonExistentToolTest(repository);
    }

    @Test(expected = ToolsRepositoryException.class)
    public void insertExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.insertExistentToolTest(repository,"Blast");
    }


    @Test
    public void deleteNonExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.deleteNonExistentToolTest(repository);
    }

    @Test
    public void deleteExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));

        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(repository);
            ToolsRepositoryTestUtils.deleteExistentToolTest(repository, toolName);
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }


    @Test(expected = ToolsRepositoryException.class)
    public void updateNonExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.updateNonExistentToolTest(repository);
    }

    @Test
    public void updateExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));

        String toolName = null;

        try {
            toolName = ToolsRepositoryTestUtils.insertDummyTool(repository);
            ToolsRepositoryTestUtils.updateExistentToolTest(repository, toolName);
        } finally {
            if(toolName != null)
                repository.delete(toolName);
        }
    }


    @Test
    public void getNonExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.getNonExistentToolTest(repository);
    }

    @Test
    public void getExistentToolTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.getExistentToolTest(repository, "Blast");
    }


    @Test
    public void getAllWithEmptyResultTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(true));
        ToolsRepositoryTestUtils.getAllWithEmptyResultTest(repository);
    }

    @Test
    public void getAllWithNonEmptyResultTest() throws ToolsRepositoryException {
        CacheToolsRepository repository = new CacheToolsRepository(new DummyToolsRepository(false));
        ToolsRepositoryTestUtils.getAllWithNonEmptyResultTest(repository, "Blast", "Velvet", "Trimmomatic");
    }


    @Test(timeout = 500)
    public void getLoadedTool() throws ToolsRepositoryException {
        ToolsRepositoryTestUtils.getExistentToolTest(loadedRepository, "Blast");
    }

}
