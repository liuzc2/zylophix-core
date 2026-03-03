package com.zylophix.core.tool;

import com.zylophix.core.tool.builtin.CalculatorTool;
import com.zylophix.core.tool.builtin.DateTimeTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ToolRegistryTest {

    private ToolRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ToolRegistry();
    }

    @Test
    void registerAndRetrieveTool() {
        registry.register(new CalculatorTool());
        Optional<Tool> tool = registry.getTool("calculator");

        assertTrue(tool.isPresent());
        assertEquals("calculator", tool.get().getDefinition().getName());
    }

    @Test
    void getToolReturnsEmptyForUnknownName() {
        Optional<Tool> tool = registry.getTool("nonexistent");
        assertFalse(tool.isPresent());
    }

    @Test
    void buildToolsDescriptionContainsAllToolNames() {
        registry.register(new CalculatorTool());
        registry.register(new DateTimeTool());

        String description = registry.buildToolsDescription();

        assertTrue(description.contains("calculator"));
        assertTrue(description.contains("datetime"));
    }

    @Test
    void buildToolsDescriptionWhenEmpty() {
        String description = registry.buildToolsDescription();
        assertEquals("No tools available.", description);
    }

    @Test
    void registerReplacesExistingToolWithSameName() {
        Tool original = new CalculatorTool();
        registry.register(original);

        Tool replacement = new CalculatorTool();
        registry.register(replacement);

        assertEquals(1, registry.getAll().size());
    }
}
