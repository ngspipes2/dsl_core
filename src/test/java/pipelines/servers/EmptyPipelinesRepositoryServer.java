package pipelines.servers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@SpringBootApplication
public class EmptyPipelinesRepositoryServer {

    private Map<String, TypedPipelineDescriptor> pipelinesByName;



    public EmptyPipelinesRepositoryServer() {
        pipelinesByName = new HashMap<>();
    }



    @RequestMapping(value = "empty/pipelines", method = RequestMethod.GET)
    public Collection<TypedPipelineDescriptor> getAll() throws Exception {
        return pipelinesByName.values();
    }

    @RequestMapping(value = "empty/pipelines/{pipelineName}", method = RequestMethod.GET)
    public TypedPipelineDescriptor get(@PathVariable String pipelineName) throws Exception {
        return pipelinesByName.get(pipelineName);
    }

    @RequestMapping(value = "empty/pipelines", method = RequestMethod.POST)
    public void insert(@RequestBody TypedPipelineDescriptor pipeline) throws Exception {
        pipelinesByName.put(pipeline.getName(), pipeline);
    }

    @RequestMapping(value = "empty/pipelines/{pipelineName}", method = RequestMethod.PUT)
    public void update(@RequestBody TypedPipelineDescriptor pipeline, @PathVariable String pipelineName) throws Exception {
        pipelinesByName.put(pipeline.getName(), pipeline);
    }

    @RequestMapping(value = "empty/pipelines/{pipelineName}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String pipelineName) throws Exception {
        pipelinesByName.remove(pipelineName);
    }

}
