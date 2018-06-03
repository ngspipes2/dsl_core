package tools.servers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import pt.isel.ngspipes.tool_descriptor.implementations.ToolDescriptor;
import pt.isel.ngspipes.tool_descriptor.interfaces.IToolDescriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@SpringBootApplication
public class NotToolsRepositoryServer {

    private Map<String, IToolDescriptor> toolsByName;



    public NotToolsRepositoryServer() {
        toolsByName = new HashMap<>();

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



    @RequestMapping(value = "notempty/tools", method = RequestMethod.GET)
    public Collection<IToolDescriptor> getAll() throws Exception {
        return toolsByName.values();
    }

    @RequestMapping(value = "notempty/tools/{toolName}", method = RequestMethod.GET)
    public IToolDescriptor get(@PathVariable String toolName) throws Exception {
        return toolsByName.get(toolName);
    }

    @RequestMapping(value = "notempty/tools", method = RequestMethod.POST)
    public void insert(@RequestBody ToolDescriptor tool) throws Exception {
        if(toolsByName.containsKey(tool.getName()))
            throw new Exception("There is already a tool with name:" + tool.getName());

        toolsByName.put(tool.getName(), tool);
    }

    @RequestMapping(value = "notempty/tools/{toolName}", method = RequestMethod.PUT)
    public void update(@RequestBody ToolDescriptor tool) throws Exception {
        if(!toolsByName.containsKey(tool.getName()))
            throw new Exception("There is not tool with name:" + tool.getName());
        toolsByName.put(tool.getName(), tool);
    }

    @RequestMapping(value = "notempty/tools/{toolName}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String toolName) throws Exception {
        toolsByName.remove(toolName);
    }

}
