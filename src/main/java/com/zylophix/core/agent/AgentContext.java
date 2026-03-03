package com.zylophix.core.agent;

import com.zylophix.core.memory.ConversationMemory;
import com.zylophix.core.tool.ToolRegistry;

/**
 * Runtime context passed to an agent during execution.
 * Holds the conversation memory and the available tool registry.
 */
public class AgentContext {

    private final ConversationMemory memory;
    private final ToolRegistry toolRegistry;

    public AgentContext(ConversationMemory memory, ToolRegistry toolRegistry) {
        this.memory = memory;
        this.toolRegistry = toolRegistry;
    }

    public ConversationMemory getMemory() {
        return memory;
    }

    public ToolRegistry getToolRegistry() {
        return toolRegistry;
    }
}
