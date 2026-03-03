package com.zylophix.core.tool.builtin;

import com.zylophix.core.tool.Tool;
import com.zylophix.core.tool.ToolDefinition;
import com.zylophix.core.tool.ToolResult;

/**
 * A stub tool that represents a web-search capability.
 *
 * <p>In a real deployment this tool would call a search API (e.g. SerpAPI, Bing, DuckDuckGo).
 * Here it returns a placeholder response so that the agent framework can be tested end-to-end
 * without external dependencies.
 *
 * <p>Input format: a plain-text search query string.
 */
public class WebSearchTool implements Tool {

    private static final ToolDefinition DEFINITION = new ToolDefinition(
            "web_search",
            "Searches the web and returns a summary of the top results for the given query.",
            "A plain-text search query string (e.g. 'latest Java 21 features')."
    );

    @Override
    public ToolDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    public ToolResult execute(String input) {
        if (input == null || input.isBlank()) {
            return ToolResult.failure("Search query must not be empty.");
        }
        // Stub implementation – replace with a real HTTP call to a search API.
        return ToolResult.success(
                "[Stub result for query: \"" + input.trim() + "\"]\n"
                + "This is a placeholder. Integrate a real search API to enable live web search."
        );
    }
}
