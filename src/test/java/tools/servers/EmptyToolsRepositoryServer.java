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
public class EmptyToolsRepositoryServer {

    private Map<String, IToolDescriptor> toolsByName;
    private byte[] logo;



    public EmptyToolsRepositoryServer() {
        toolsByName = new HashMap<>();
    }



    @RequestMapping(value = "empty/logo", method = RequestMethod.GET)
    public byte[] getLogo() throws Exception {
        return logo;
    }

    @RequestMapping(value = "empty/logo", method = RequestMethod.POST)
    public void setLogo(@RequestBody(required = false) byte[] logo) throws Exception {
        this.logo = logo;
    }

    @RequestMapping(value = "empty/names", method = RequestMethod.GET)
    public Collection<String> getNames() throws Exception {
        return toolsByName.keySet();
    }

    @RequestMapping(value = "empty/tools", method = RequestMethod.GET)
    public Collection<IToolDescriptor> getAll() throws Exception {
        return toolsByName.values();
    }

    @RequestMapping(value = "empty/tools/{toolName}", method = RequestMethod.GET)
    public IToolDescriptor get(@PathVariable String toolName) throws Exception {
        return toolsByName.get(toolName);
    }

    @RequestMapping(value = "empty/tools", method = RequestMethod.POST)
    public void insert(@RequestBody ToolDescriptor tool) throws Exception {
        toolsByName.put(tool.getName(), tool);
    }

    @RequestMapping(value = "empty/tools/{toolName}", method = RequestMethod.PUT)
    public void update(@RequestBody ToolDescriptor tool) throws Exception {
        toolsByName.put(tool.getName(), tool);
    }

    @RequestMapping(value = "empty/tools/{toolName}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String toolName) throws Exception {
        toolsByName.remove(toolName);
    }

}
