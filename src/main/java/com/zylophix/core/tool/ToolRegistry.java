package com.zylophix.core.tool;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry that holds all tools available to an agent.
 *
 * <p>Tools are looked up by their {@link ToolDefinition#getName() name}.
 * Registration order is preserved when generating the tools description for the LLM prompt.
 */
public class ToolRegistry {

    private final Map<String, Tool> tools = new LinkedHashMap<>();

    /**
     * Register a tool. If a tool with the same name already exists it will be replaced.
     *
     * @param tool the tool to register
     */
    public void register(Tool tool) {
        tools.put(tool.getDefinition().getName(), tool);
    }

    /**
     * Look up a tool by name.
     *
     * @param name the tool name
     * @return an Optional containing the tool, or empty if not found
     */
    public Optional<Tool> getTool(String name) {
        return Optional.ofNullable(tools.get(name));
    }

    /**
     * Return an unmodifiable view of all registered tools.
     */
    public Collection<Tool> getAll() {
        return Collections.unmodifiableCollection(tools.values());
    }

    /**
     * Build a formatted description of all registered tools for inclusion in the system prompt.
     */
    public String buildToolsDescription() {
        if (tools.isEmpty()) {
            return "No tools available.";
        }
        StringBuilder sb = new StringBuilder("Available tools:\n");
        for (Tool tool : tools.values()) {
            sb.append(tool.getDefinition().toPromptDescription()).append("\n");
        }
        return sb.toString().trim();
    }
}
