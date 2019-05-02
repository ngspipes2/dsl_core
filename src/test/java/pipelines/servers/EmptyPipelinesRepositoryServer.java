package pipelines.servers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@SpringBootApplication
public class EmptyPipelinesRepositoryServer {

    private Map<String, IPipelineDescriptor> pipelinesByName;
    private byte[] logo;



    public EmptyPipelinesRepositoryServer() {
        pipelinesByName = new HashMap<>();
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
        return pipelinesByName.keySet();
    }

    @RequestMapping(value = "empty/pipelines", method = RequestMethod.GET)
    public Collection<IPipelineDescriptor> getAll() throws Exception {
        return pipelinesByName.values();
    }

    @RequestMapping(value = "empty/pipelines/{pipelineName}", method = RequestMethod.GET)
    public IPipelineDescriptor get(@PathVariable String pipelineName) throws Exception {
        return pipelinesByName.get(pipelineName);
    }

    @RequestMapping(value = "empty/pipelines", method = RequestMethod.POST)
    public void insert(@RequestBody IPipelineDescriptor pipeline) throws Exception {
        pipelinesByName.put(pipeline.getName(), pipeline);
    }

    @RequestMapping(value = "empty/pipelines/{pipelineName}", method = RequestMethod.PUT)
    public void update(@RequestBody IPipelineDescriptor pipeline, @PathVariable String pipelineName) throws Exception {
        pipelinesByName.put(pipeline.getName(), pipeline);
    }

    @RequestMapping(value = "empty/pipelines/{pipelineName}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String pipelineName) throws Exception {
        pipelinesByName.remove(pipelineName);
    }

}
