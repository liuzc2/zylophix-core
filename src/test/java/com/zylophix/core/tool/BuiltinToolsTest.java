package com.zylophix.core.tool;

import com.zylophix.core.tool.builtin.CalculatorTool;
import com.zylophix.core.tool.builtin.DateTimeTool;
import com.zylophix.core.tool.builtin.WebSearchTool;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuiltinToolsTest {

    // ---- CalculatorTool ----

    @Test
    void calculatorEvaluatesSimpleExpression() {
        ToolResult result = new CalculatorTool().execute("2 + 3");
        assertTrue(result.isSuccess());
        assertEquals("5", result.getOutput());
    }

    @Test
    void calculatorEvaluatesComplexExpression() {
        ToolResult result = new CalculatorTool().execute("(10 + 5) * 2");
        assertTrue(result.isSuccess());
        assertEquals("30", result.getOutput());
    }

    @Test
    void calculatorRejectsInvalidInput() {
        ToolResult result = new CalculatorTool().execute("alert('xss')");
        assertFalse(result.isSuccess());
    }

    @Test
    void calculatorRejectsEmptyInput() {
        ToolResult result = new CalculatorTool().execute("");
        assertFalse(result.isSuccess());
    }

    // ---- DateTimeTool ----

    @Test
    void dateTimeReturnsResultForValidTimezone() {
        ToolResult result = new DateTimeTool().execute("UTC");
        assertTrue(result.isSuccess());
        assertNotNull(result.getOutput());
    }

    @Test
    void dateTimeDefaultsToUtcForBlankInput() {
        ToolResult result = new DateTimeTool().execute("  ");
        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("UTC"));
    }

    @Test
    void dateTimeRejectsUnknownTimezone() {
        ToolResult result = new DateTimeTool().execute("Invalid/Zone");
        assertFalse(result.isSuccess());
    }

    // ---- WebSearchTool ----

    @Test
    void webSearchReturnsStubResultForQuery() {
        ToolResult result = new WebSearchTool().execute("Java 21 features");
        assertTrue(result.isSuccess());
        assertTrue(result.getOutput().contains("Java 21 features"));
    }

    @Test
    void webSearchRejectsEmptyQuery() {
        ToolResult result = new WebSearchTool().execute("");
        assertFalse(result.isSuccess());
    }
}
