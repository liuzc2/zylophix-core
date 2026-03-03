package com.zylophix.core.tool;

/**
 * Describes a tool so that the LLM can understand what it does and how to invoke it.
 */
public class ToolDefinition {

    private final String name;
    private final String description;
    private final String parametersSchema;

    public ToolDefinition(String name, String description, String parametersSchema) {
        this.name = name;
        this.description = description;
        this.parametersSchema = parametersSchema;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * A human-readable description of the expected parameters (e.g. JSON schema or plain text).
     */
    public String getParametersSchema() {
        return parametersSchema;
    }

    /**
     * Returns a formatted string suitable for inclusion in an LLM system prompt.
     */
    public String toPromptDescription() {
        return "- " + name + ": " + description + "\n  Parameters: " + parametersSchema;
    }

    @Override
    public String toString() {
        return "ToolDefinition{name='" + name + "', description='" + description + "'}";
    }
}
