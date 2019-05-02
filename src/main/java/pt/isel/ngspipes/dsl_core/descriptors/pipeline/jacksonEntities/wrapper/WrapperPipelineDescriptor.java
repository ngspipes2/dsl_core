package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.output.WrapperOutputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.parameter.WrapperParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.repository.WrapperPipelineRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.repository.WrapperToolRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jacksonEntities.wrapper.step.WrapperStepDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.IPipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.PipelineDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IPipelineRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IRepositoryDescriptor;
import pt.isel.ngspipes.pipeline_descriptor.repository.IToolRepositoryDescriptor;

import java.util.stream.Collectors;

public class WrapperPipelineDescriptor extends PipelineDescriptor {

    private static IRepositoryDescriptor wrapRepository(IRepositoryDescriptor repository) {
        if(repository instanceof IToolRepositoryDescriptor)
            return new WrapperToolRepositoryDescriptor((IToolRepositoryDescriptor) repository);
        else if(repository instanceof IPipelineRepositoryDescriptor)
            return new WrapperPipelineRepositoryDescriptor((IPipelineRepositoryDescriptor) repository);
        else
            throw new IllegalArgumentException("Unknown repository descriptor " + repository.getClass().getName());
    }



    private IPipelineDescriptor source;



    public WrapperPipelineDescriptor(IPipelineDescriptor source) {
        super(
            source.getName(),
            source.getDescription(),
            source.getAuthor(),
            source.getVersion(),
            source.getDocumentation(),
            source.getLogo(),
            source.getParameters() == null ?
                null : source.getParameters().stream().map(WrapperParameterDescriptor::new).collect(Collectors.toList()),
            source.getOutputs() == null ?
                null : source.getOutputs().stream().map(WrapperOutputDescriptor::new).collect(Collectors.toList()),
            source.getRepositories() == null ?
                null : source.getRepositories().stream().map(WrapperPipelineDescriptor::wrapRepository).collect(Collectors.toList()),
            source.getSteps() == null ?
                null : source.getSteps().stream().map(WrapperStepDescriptor::new).collect(Collectors.toList())
        );

        this.source = source;
    }

}
