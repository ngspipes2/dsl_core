package pipelines;

import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.IOutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.output.OutputDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.IParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.parameter.ParameterDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.*;
import pt.isel.ngspipes.pipeline_descriptor.step.IStepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.StepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.CommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.ICommandExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.IPipelineExecDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.step.exec.PipelineExecDescriptor;
import pt.isel.ngspipes.pipeline_repository.IPipelinesRepository;
import pt.isel.ngspipes.pipeline_repository.PipelinesRepositoryException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PipelinesRepositoryTestUtils {

    public static String insertDummyPipeline(IPipelinesRepository repository) throws PipelinesRepositoryException {
        IPipelineDescriptor pipeline = getDummyPipeline();

        repository.insert(pipeline);

        return pipeline.getName();
    }

    public static IPipelineDescriptor getDummyPipeline() throws PipelinesRepositoryException {
        String pipelineName = UUID.randomUUID().toString();
        return getDummyPipeline(pipelineName);
    }

    public static IPipelineDescriptor getDummyPipeline(String pipelineName) throws PipelinesRepositoryException {
        IPipelineDescriptor pipeline = new PipelineDescriptor();
        pipeline.setName(pipelineName);
        pipeline.setOutputs(getDummyOutputs());
        pipeline.setParameters(getDummyParameters());
        pipeline.setRepositories(getDummyRepositories());
        pipeline.setSteps(getDummySteps());

        return pipeline;
    }

    public static Collection<IOutputDescriptor> getDummyOutputs() {
        Collection<IOutputDescriptor> outputs = new LinkedList<>();

        IOutputDescriptor output = new OutputDescriptor();
        output.setName("Dummy_Output");

        outputs.add(output);

        return outputs;
    }

    public static Collection<IParameterDescriptor> getDummyParameters() {
        Collection<IParameterDescriptor> parameters = new LinkedList<>();

        IParameterDescriptor parameter = new ParameterDescriptor();
        parameter.setName("Dummy_Parameter");
        parameter.setDefaultValue("dummy parameter value");

        parameters.add(parameter);

        return parameters;
    }

    public static Collection<IRepositoryDescriptor> getDummyRepositories() {
        Collection<IRepositoryDescriptor> repositories = new LinkedList<>();

        IRepositoryDescriptor repository = new ToolRepositoryDescriptor();
        repository.setId("Dummy_Tool_Repository_Descriptor");

        repositories.add(repository);

        repository = new PipelineRepositoryDescriptor();
        repository.setId("Dummy_Pipeline_Repository_Descriptor");

        repositories.add(repository);

        return repositories;
    }

    public static Collection<IStepDescriptor> getDummySteps() {
        Collection<IStepDescriptor> steps = new LinkedList<>();

        IStepDescriptor step = new StepDescriptor();
        step.setId("Dummy_Step_1");
        step.setExec(new CommandExecDescriptor("RepositoryId", "ToolId", "CommandId"));

        steps.add(step);

        step = new StepDescriptor();
        step.setId("Dummy_Step_2");
        step.setExec(new PipelineExecDescriptor("RepositoryId", "PipelineId"));

        steps.add(step);

        return steps;
    }


    public static void getExistentLogoTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        byte[] logo = repository.getLogo();

        assertNotNull(logo);
        assertNotEquals(0, logo.length);
    }

    public static void getNonExistentLogoTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        byte[] logo = repository.getLogo();

        assertNull(logo);
    }

    public static void setLogoTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        byte[] originalLogo = repository.getLogo();

        try{
            byte[] logo = new byte[]{1,2,3};

            repository.setLogo(logo);

            byte[] receivedLogo = repository.getLogo();

            assertNotNull(receivedLogo);
            assertEquals(logo.length, receivedLogo.length);

            for(int i=0; i<logo.length; ++i)
                assertEquals(logo[i], receivedLogo[i]);
        } finally {
            repository.setLogo(originalLogo);
        }
    }

    public static void setNullLogoTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        byte[] originalLogo = repository.getLogo();

        try{
            repository.setLogo(null);

            byte[] receivedLogo = repository.getLogo();

            assertNull(receivedLogo);
        } finally {
            repository.setLogo(originalLogo);
        }
    }


    public static void insertNonExistentPipelineTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        String pipelineName = null;

        try {
            pipelineName = insertDummyPipeline(repository);

            IPipelineDescriptor pipeline = repository.get(pipelineName);

            assertNotNull(pipeline);
            assertEquals(pipelineName, pipeline.getName());
            assertEquals(1, pipeline.getOutputs().size());
            assertEquals("Dummy_Output", pipeline.getOutputs().stream().findFirst().get().getName());
            assertEquals(1, pipeline.getParameters().size());
            assertEquals("Dummy_Parameter", pipeline.getParameters().stream().findFirst().get().getName());
            assertEquals("dummy parameter value", pipeline.getParameters().stream().findFirst().get().getDefaultValue());
            assertEquals(2, pipeline.getRepositories().size());
            assertEquals("Dummy_Tool_Repository_Descriptor", pipeline.getRepositories().stream().findFirst().get().getId());
            assertTrue(IToolRepositoryDescriptor.class.isAssignableFrom(pipeline.getRepositories().stream().findFirst().get().getClass()));
            assertEquals("Dummy_Pipeline_Repository_Descriptor", pipeline.getRepositories().stream().skip(1).findFirst().get().getId());
            assertTrue(IPipelineRepositoryDescriptor.class.isAssignableFrom(pipeline.getRepositories().stream().skip(1).findFirst().get().getClass()));
            assertEquals(2, pipeline.getSteps().size());
            assertEquals("Dummy_Step_1", pipeline.getSteps().stream().findFirst().get().getId());
            assertTrue(ICommandExecDescriptor.class.isAssignableFrom(pipeline.getSteps().stream().findFirst().get().getExec().getClass()));
            assertEquals("Dummy_Step_2", pipeline.getSteps().stream().skip(1).findFirst().get().getId());
            assertTrue(IPipelineExecDescriptor.class.isAssignableFrom(pipeline.getSteps().stream().skip(1).findFirst().get().getExec().getClass()));
        } finally {
            if(pipelineName != null)
                repository.delete(pipelineName);
        }
    }

    public static void insertExistentPipelineTest(IPipelinesRepository repository, String existentPipelineName) throws PipelinesRepositoryException {
        IPipelineDescriptor pipeline = getDummyPipeline(existentPipelineName);
        repository.insert(pipeline);
    }


    public static void deleteNonExistentPipelineTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        String pipelineName = UUID.randomUUID().toString();

        repository.delete(pipelineName);
    }

    public static void deleteExistentPipelineTest(IPipelinesRepository repository, String pipelineName) throws PipelinesRepositoryException {
        repository.delete(pipelineName);

        IPipelineDescriptor pipeline = repository.get(pipelineName);

        assertNull(pipeline);
    }


    public static void updateNonExistentPipelineTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        String pipelineName = UUID.randomUUID().toString();
        String author = UUID.randomUUID().toString();

        IPipelineDescriptor pipeline = getDummyPipeline(pipelineName);
        pipeline.setAuthor(author);

        repository.update(pipeline);
    }

    public static void updateExistentPipelineTest(IPipelinesRepository repository, String pipelineName) throws PipelinesRepositoryException {
        String author = UUID.randomUUID().toString();

        IPipelineDescriptor pipeline = new PipelineDescriptor();
        pipeline.setName(pipelineName);
        pipeline.setAuthor(author);
        pipeline.setOutputs(new LinkedList<>());
        pipeline.setParameters(new LinkedList<>());
        pipeline.setRepositories(new LinkedList<>());
        pipeline.setSteps(new LinkedList<>());

        repository.update(pipeline);

        pipeline = repository.get(pipelineName);

        assertNotNull(pipeline);
        assertEquals(pipelineName, pipeline.getName());
        assertEquals(author, pipeline.getAuthor());
        assertEquals(0, pipeline.getOutputs().size());
        assertEquals(0, pipeline.getParameters().size());
        assertEquals(0, pipeline.getRepositories().size());
        assertEquals(0, pipeline.getSteps().size());
    }


    public static void getNonExistentPipelineTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        String pipelineName = UUID.randomUUID().toString();

        IPipelineDescriptor pipeline = repository.get(pipelineName);

        assertNull(pipeline);
    }

    public static void getExistentPipelineTest(IPipelinesRepository repository, String pipelineName) throws PipelinesRepositoryException {
        IPipelineDescriptor pipeline = repository.get(pipelineName);

        assertNotNull(pipeline);
        assertEquals(pipelineName, pipeline.getName());
    }


    public static void getAllWithEmptyResultTest(IPipelinesRepository repository) throws PipelinesRepositoryException {
        Collection<IPipelineDescriptor> pipelines = repository.getAll();

        assertNotNull(pipelines);
        assertEquals(0, pipelines.size());
    }

    public static void getAllWithNonEmptyResultTest(IPipelinesRepository repository, String... expectedPipelinesNames) throws PipelinesRepositoryException {
        Collection<IPipelineDescriptor> pipelines = repository.getAll();

        assertNotNull(pipelines);
        assertEquals(expectedPipelinesNames.length, pipelines.size());

        Collection<String> pipelinesNames = pipelines.stream().map(IPipelineDescriptor::getName).collect(Collectors.toList());
        for(String pipelineName : expectedPipelinesNames)
            assertTrue(pipelinesNames.contains(pipelineName));
    }

}
