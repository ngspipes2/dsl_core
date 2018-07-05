package pipelines.servers;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@SpringBootApplication
public class NotEmptyPipelinesRepositoryServer {

    private Map<String, TypedPipelineDescriptor> pipelinesByName;



    public NotEmptyPipelinesRepositoryServer() {
        pipelinesByName = new HashMap<>();

        TypedPipelineDescriptor pipelineDescriptor = new TypedPipelineDescriptor();
        pipelineDescriptor.setName("PipelineA");
        pipelinesByName.put(pipelineDescriptor.getName(), pipelineDescriptor);

        pipelineDescriptor = new TypedPipelineDescriptor();
        pipelineDescriptor.setName("PipelineB");
        pipelinesByName.put(pipelineDescriptor.getName(), pipelineDescriptor);
    }



    @RequestMapping(value = "notempty/pipelines", method = RequestMethod.GET)
    public Collection<TypedPipelineDescriptor> getAll() throws Exception {
        return pipelinesByName.values();
    }

    @RequestMapping(value = "notempty/pipelines/{pipelineName}", method = RequestMethod.GET)
    public TypedPipelineDescriptor get(@PathVariable String pipelineName) throws Exception {
        return pipelinesByName.get(pipelineName);
    }

    @RequestMapping(value = "notempty/pipelines", method = RequestMethod.POST)
    public void insert(@RequestBody TypedPipelineDescriptor pipeline) throws Exception {
        if(pipelinesByName.containsKey(pipeline.getName()))
            throw new PipelinesRepositoryException("There is already a pipeline with name:" + pipeline + "!");

        pipelinesByName.put(pipeline.getName(), pipeline);
    }

    @RequestMapping(value = "notempty/pipelines/{pipelineName}", method = RequestMethod.PUT)
    public void update(@RequestBody TypedPipelineDescriptor pipeline, @PathVariable String pipelineName) throws Exception {
        if(!pipelinesByName.containsKey(pipeline.getName()))
            throw new PipelinesRepositoryException("There is no pipeline with name:" + pipeline + "!");

        pipelinesByName.put(pipeline.getName(), pipeline);
    }

    @RequestMapping(value = "notempty/pipelines/{pipelineName}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String pipelineName) throws Exception {
        pipelinesByName.remove(pipelineName);
    }

}
