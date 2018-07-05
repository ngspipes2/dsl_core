package pt.isel.ngspipes.dsl_core.descriptors.pipeline.jackson_entities.typed.step.exec;

public class TypedCommandExecDescriptor extends TypedExecDescriptor {

    private String toolName;
    public String getToolName() { return this.toolName; }
    public void setToolName(String toolName) { this.toolName = toolName; }

    private String commandName;
    public String getCommandName() { return this.commandName; }
    public void setCommandName(String commandName) { this.commandName = commandName; }



    public TypedCommandExecDescriptor(String repositoryId, String toolName, String commandName) {
        super(repositoryId);
        this.toolName = toolName;
        this.commandName = commandName;
    }

    public TypedCommandExecDescriptor() { }

}
