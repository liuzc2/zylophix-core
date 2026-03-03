package com.zylophix.core.tool;

/**
 * A capability the agent can invoke during its reasoning loop.
 *
 * <p>Implementations should be stateless so that they can be safely shared
 * across multiple agent invocations.
 */
public interface Tool {

    /**
     * Return a descriptor that the LLM uses to understand this tool.
     */
    ToolDefinition getDefinition();

    /**
     * Execute this tool with the given input string.
     *
     * @param input the raw input provided by the LLM (may be a plain string or JSON)
     * @return the result of executing the tool
     */
    ToolResult execute(String input);
}
