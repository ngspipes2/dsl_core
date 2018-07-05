package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed;

import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.output.TypedOutputDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.parameter.TypedParameterDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.repository.TypedRepositoryDescriptor;
import pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.TypedStepDescriptor;

import java.util.Collection;

public class TypedPipelineDescriptor {

    private String name;
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    private String description;
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    private String author;
    public String getAuthor() { return this.author; }
    public void setAuthor(String author) { this.author = author; }

    private String version;
    public String getVersion(){ return this.version; }
    public void setVersion(String version){ this.version = version; }

    private Collection<String> documentation;
    public Collection<String> getDocumentation(){ return this.documentation; }
    public void setDocumentation(Collection<String> documentation){ this.documentation = documentation; }

    private byte[] logo;
    public byte[] getLogo() { return this.logo; }
    public void setLogo(byte[] logo) { this.logo = logo; }

    private Collection<TypedParameterDescriptor> parameters;
    public Collection<TypedParameterDescriptor> getParameters() { return this.parameters; }
    public void setParameters(Collection<TypedParameterDescriptor> parameters) { this.parameters = parameters; }

    private Collection<TypedOutputDescriptor> outputs;
    public Collection<TypedOutputDescriptor> getOutputs() { return this.outputs; }
    public void setOutputs(Collection<TypedOutputDescriptor> outputs) { this.outputs = outputs; }

    private Collection<TypedRepositoryDescriptor> repositories;
    public Collection<TypedRepositoryDescriptor> getRepositories() { return this.repositories; }
    public void setRepositories(Collection<TypedRepositoryDescriptor> repositories) { this.repositories = repositories; }

    private Collection<TypedStepDescriptor> steps;
    public Collection<TypedStepDescriptor> getSteps() { return this.steps; }
    public void setSteps(Collection<TypedStepDescriptor> steps) { this.steps = steps; }



    public TypedPipelineDescriptor(
        String name,
        String description,
        String author,
        String version,
        Collection<String> documentation,
        byte[] logo,
        Collection<TypedParameterDescriptor> parameters,
        Collection<TypedOutputDescriptor> outputs,
        Collection<TypedRepositoryDescriptor> repositories,
        Collection<TypedStepDescriptor> steps) {
        this.name = name;
        this.description = description;
        this.author= author;
        this.version = version;
        this.documentation = documentation;
        this.logo = logo;
        this.parameters = parameters;
        this.outputs = outputs;
        this.repositories = repositories;
        this.steps = steps;
    }

    public TypedPipelineDescriptor(){}

}
