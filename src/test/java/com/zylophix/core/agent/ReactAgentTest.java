package com.zylophix.core.agent;

import com.zylophix.core.llm.LLMClient;
import com.zylophix.core.llm.LLMResponse;
import com.zylophix.core.memory.ConversationMemory;
import com.zylophix.core.tool.ToolRegistry;
import com.zylophix.core.tool.builtin.CalculatorTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactAgentTest {

    @Mock
    private LLMClient llmClient;

    private ToolRegistry toolRegistry;
    private AgentConfig config;
    private ReactAgent agent;

    @BeforeEach
    void setUp() {
        toolRegistry = new ToolRegistry();
        toolRegistry.register(new CalculatorTool());

        config = AgentConfig.builder()
                .name("TestAgent")
                .maxIterations(5)
                .build();

        AgentContext context = new AgentContext(new ConversationMemory(), toolRegistry);
        agent = new ReactAgent(config, llmClient, context);
    }

    @Test
    void runReturnsSuccessWhenLLMProducesFinalAnswerImmediately() {
        when(llmClient.chat(any()))
                .thenReturn(LLMResponse.of("Final Answer: 42"));

        AgentResult result = agent.run("What is the answer to life?");

        assertTrue(result.isSuccess());
        assertEquals("42", result.getAnswer());
        assertEquals(1, result.getIterationsUsed());
    }

    @Test
    void runUsesToolAndThenReturnsFinalAnswer() {
        when(llmClient.chat(any()))
                .thenReturn(LLMResponse.of(
                        "Thought: I should calculate this\nAction: calculator\nAction Input: 2 + 2"))
                .thenReturn(LLMResponse.of("Final Answer: 4"));

        AgentResult result = agent.run("What is 2 + 2?");

        assertTrue(result.isSuccess());
        assertEquals("4", result.getAnswer());
        assertEquals(2, result.getIterationsUsed());
        verify(llmClient, times(2)).chat(any());
    }

    @Test
    void runReturnsFailureWhenMaxIterationsExceeded() {
        when(llmClient.chat(any()))
                .thenReturn(LLMResponse.of("Thought: Hmm\nAction: calculator\nAction Input: 1+1"));

        AgentResult result = agent.run("Loop forever");

        assertFalse(result.isSuccess());
        assertEquals(5, result.getIterationsUsed());
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void runHandlesUnknownToolGracefully() {
        when(llmClient.chat(any()))
                .thenReturn(LLMResponse.of("Thought: Use a tool\nAction: nonexistent_tool\nAction Input: data"))
                .thenReturn(LLMResponse.of("Final Answer: Could not find the tool"));

        AgentResult result = agent.run("Use nonexistent tool");

        assertTrue(result.isSuccess());
    }

    @Test
    void runTreatsOutputWithoutActionAsFinalAnswer() {
        when(llmClient.chat(any()))
                .thenReturn(LLMResponse.of("This is a direct response without any action tags."));

        AgentResult result = agent.run("Tell me something");

        assertTrue(result.isSuccess());
    }

    @Test
    void getName() {
        assertEquals("TestAgent", agent.getName());
    }
}
