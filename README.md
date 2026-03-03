# zylophix-core

A Java AI Agent development framework implementing the **ReAct (Reasoning + Acting)** pattern.

## Overview

Zylophix Core provides the building blocks for developing LLM-powered agents that can reason about problems and take actions using pluggable tools. It is designed for self-study and experimentation with AI agent architectures.

### Architecture

```
zylophix-core/
├── src/main/java/com/zylophix/core/
│   ├── agent/          # Agent interface, config, context, result, and ReAct implementation
│   ├── llm/            # LLM client abstraction (LLMClient, LLMMessage, LLMRequest, LLMResponse)
│   ├── memory/         # Conversation history management (ConversationMemory)
│   └── tool/           # Tool abstraction and registry, plus built-in tools
│       └── builtin/    # CalculatorTool, DateTimeTool, WebSearchTool (stub)
└── src/test/           # JUnit 5 unit tests
```

### The ReAct Loop

The `ReactAgent` follows this iterative cycle:

1. **Thought** – the LLM reasons about what to do next.
2. **Action** – the LLM selects a tool and provides input for it.
3. **Observation** – the tool is executed and the result is fed back to the LLM.

The loop repeats until the LLM produces a **Final Answer**, or the maximum iteration limit is reached.

Expected LLM output format:
```
Thought: <reasoning>
Action: <tool_name>
Action Input: <tool_input>
```
Or to terminate:
```
Final Answer: <answer>
```

## Prerequisites

- Java 17+
- Apache Maven 3.8+

## Build & Test

```bash
mvn clean test
```

## Quick Start

```java
// 1. Create an LLM client (implement LLMClient for your provider, e.g. OpenAI)
LLMClient llmClient = new YourLLMClient();

// 2. Register tools
ToolRegistry tools = new ToolRegistry();
tools.register(new CalculatorTool());
tools.register(new DateTimeTool());
tools.register(new WebSearchTool());

// 3. Configure and run the agent
AgentConfig config = AgentConfig.builder()
        .name("MyAgent")
        .systemPrompt("You are a helpful AI assistant.")
        .maxIterations(10)
        .build();

AgentContext context = new AgentContext(new ConversationMemory(), tools);
Agent agent = new ReactAgent(config, llmClient, context);

AgentResult result = agent.run("What is 15 * 7?");
System.out.println(result.getAnswer()); // 105
```

## Integrating a Real LLM

Implement the `LLMClient` interface to connect to any LLM provider:

```java
public class OpenAIClient implements LLMClient {
    @Override
    public LLMResponse chat(LLMRequest request) {
        // Call OpenAI API and return LLMResponse
    }
}
```

## Built-in Tools

| Tool | Name | Description |
|------|------|-------------|
| `CalculatorTool` | `calculator` | Evaluates arithmetic expressions (+, -, *, /, parentheses) |
| `DateTimeTool` | `datetime` | Returns current date/time for a given timezone |
| `WebSearchTool` | `web_search` | Stub – integrate a real search API (SerpAPI, Bing, etc.) |

## Adding Custom Tools

Implement the `Tool` interface:

```java
public class MyCustomTool implements Tool {
    @Override
    public ToolDefinition getDefinition() {
        return new ToolDefinition("my_tool", "Does something useful.", "A string input.");
    }

    @Override
    public ToolResult execute(String input) {
        return ToolResult.success("Result: " + input.toUpperCase());
    }
}
```

Then register it with the `ToolRegistry`:

```java
tools.register(new MyCustomTool());
```
