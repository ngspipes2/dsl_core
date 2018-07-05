package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.fileBased;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.TypedPipelineDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.output.TypedOutputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.parameter.TypedParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.TypedRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.TypedStepDescriptor;

import java.util.Collection;

public class FileBasedPipelineDescriptor extends TypedPipelineDescriptor {

    private TypedPipelineDescriptor sourceDescriptor;



    public FileBasedPipelineDescriptor(TypedPipelineDescriptor sourceDescriptor) {
        this.sourceDescriptor = sourceDescriptor;
    }

    public FileBasedPipelineDescriptor() {
        this(new TypedPipelineDescriptor());
    }



    @Override
    public String getName() {
        return sourceDescriptor.getName();
    }

    @Override
    public void setName(String name) {
        sourceDescriptor.setName(name);
    }

    @Override
    public String getDescription() {
        return sourceDescriptor.getDescription();
    }

    @Override
    public void setDescription(String description) {
        sourceDescriptor.setDescription(description);
    }

    @Override
    public String getAuthor() {
        return sourceDescriptor.getAuthor();
    }

    @Override
    public void setAuthor(String author) {
        sourceDescriptor.setAuthor(author);
    }

    @Override
    public String getVersion() {
        return sourceDescriptor.getVersion();
    }

    @Override
    public void setVersion(String version) {
        sourceDescriptor.setVersion(version);
    }

    @Override
    public Collection<String> getDocumentation() {
        return sourceDescriptor.getDocumentation();
    }

    @Override
    public void setDocumentation(Collection<String> documentation) {
        sourceDescriptor.setDocumentation(documentation);
    }

    @JsonIgnore
    @Override
    public byte[] getLogo() {
        return sourceDescriptor.getLogo();
    }

    @Override
    public void setLogo(byte[] logo) {
        sourceDescriptor.setLogo(logo);
    }

    @Override
    public Collection<TypedParameterDescriptor> getParameters() {
        return sourceDescriptor.getParameters();
    }

    @Override
    public void setParameters(Collection<TypedParameterDescriptor> parameters) {
        sourceDescriptor.setParameters(parameters);
    }

    @Override
    public Collection<TypedOutputDescriptor> getOutputs() {
        return sourceDescriptor.getOutputs();
    }

    @Override
    public void setOutputs(Collection<TypedOutputDescriptor> outputs) {
        sourceDescriptor.setOutputs(outputs);
    }

    @Override
    public Collection<TypedRepositoryDescriptor> getRepositories() {
        return sourceDescriptor.getRepositories();
    }

    @Override
    public void setRepositories(Collection<TypedRepositoryDescriptor> repositories) {
        sourceDescriptor.setRepositories(repositories);
    }

    @Override
    public Collection<TypedStepDescriptor> getSteps() {
        return sourceDescriptor.getSteps();
    }

    @Override
    public void setSteps(Collection<TypedStepDescriptor> steps) {
        sourceDescriptor.setSteps(steps);
    }

}
