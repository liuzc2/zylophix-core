package com.zylophix.core.agent;

import com.zylophix.core.llm.LLMClient;
import com.zylophix.core.llm.LLMMessage;
import com.zylophix.core.llm.LLMRequest;
import com.zylophix.core.llm.LLMResponse;
import com.zylophix.core.tool.Tool;
import com.zylophix.core.tool.ToolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A ReAct (Reasoning + Acting) agent.
 *
 * <p>The agent follows the loop:
 * <ol>
 *   <li><b>Thought</b> – the LLM reasons about what to do next.</li>
 *   <li><b>Action</b> – the LLM selects a tool and provides input for it.</li>
 *   <li><b>Observation</b> – the tool is executed and its result is fed back to the LLM.</li>
 * </ol>
 * The loop repeats until the LLM emits a {@code Final Answer:} or the maximum number of
 * iterations is reached.
 *
 * <p>Expected LLM output format (per iteration):
 * <pre>
 * Thought: &lt;reasoning text&gt;
 * Action: &lt;tool_name&gt;
 * Action Input: &lt;input for the tool&gt;
 * </pre>
 * Or to terminate:
 * <pre>
 * Final Answer: &lt;the answer to return to the user&gt;
 * </pre>
 */
public class ReactAgent implements Agent {

    private static final Logger log = LoggerFactory.getLogger(ReactAgent.class);

    private static final String FINAL_ANSWER_PREFIX = "Final Answer:";
    private static final String ACTION_PREFIX = "Action:";
    private static final String ACTION_INPUT_PREFIX = "Action Input:";

    private final AgentConfig config;
    private final LLMClient llmClient;
    private final AgentContext context;

    public ReactAgent(AgentConfig config, LLMClient llmClient, AgentContext context) {
        this.config = config;
        this.llmClient = llmClient;
        this.context = context;
    }

    @Override
    public String getName() {
        return config.getName();
    }

    @Override
    public AgentResult run(String query) {
        log.info("[{}] Starting with query: {}", config.getName(), query);

        context.getMemory().clear();

        String systemPrompt = buildSystemPrompt();
        context.getMemory().add(LLMMessage.system(systemPrompt));
        context.getMemory().add(LLMMessage.user(query));

        int iteration = 0;
        while (iteration < config.getMaxIterations()) {
            iteration++;
            log.debug("[{}] Iteration {}/{}", config.getName(), iteration, config.getMaxIterations());

            LLMRequest request = LLMRequest.builder()
                    .messages(context.getMemory().getMessages())
                    .build();
            LLMResponse response = llmClient.chat(request);
            String llmOutput = response.getContent().trim();

            log.debug("[{}] LLM output:\n{}", config.getName(), llmOutput);
            context.getMemory().add(LLMMessage.assistant(llmOutput));

            // Check for final answer
            if (containsFinalAnswer(llmOutput)) {
                String answer = extractFinalAnswer(llmOutput);
                log.info("[{}] Final answer reached after {} iteration(s).", config.getName(), iteration);
                return AgentResult.success(answer, iteration);
            }

            // Parse action and action input
            String toolName = extractLine(llmOutput, ACTION_PREFIX);
            String toolInput = extractLine(llmOutput, ACTION_INPUT_PREFIX);

            if (toolName == null) {
                // LLM output doesn't follow the expected format – treat entire output as final answer
                log.warn("[{}] Could not parse Action from LLM output; treating as final answer.", config.getName());
                return AgentResult.success(llmOutput, iteration);
            }

            // Execute the tool
            Optional<Tool> tool = context.getToolRegistry().getTool(toolName.trim());
            String observation;
            if (tool.isPresent()) {
                ToolResult result = tool.get().execute(toolInput != null ? toolInput : "");
                observation = result.toObservation();
                log.debug("[{}] Tool '{}' observation: {}", config.getName(), toolName, observation);
            } else {
                observation = "Error: Tool '" + toolName + "' not found. Available tools: "
                        + context.getToolRegistry().buildToolsDescription();
                log.warn("[{}] Tool '{}' not found.", config.getName(), toolName);
            }

            // Feed observation back to the LLM
            context.getMemory().add(LLMMessage.user("Observation: " + observation));
        }

        log.warn("[{}] Reached maximum iterations ({}) without a final answer.", config.getName(), config.getMaxIterations());
        return AgentResult.failure("Maximum iterations reached without producing a final answer.", iteration);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private String buildSystemPrompt() {
        return config.getSystemPrompt() + "\n\n"
                + context.getToolRegistry().buildToolsDescription() + "\n\n"
                + "Use the following format:\n"
                + "Thought: your reasoning about what to do\n"
                + "Action: the tool name to use\n"
                + "Action Input: the input to pass to the tool\n"
                + "Observation: the result of the action (provided by the system)\n"
                + "... (repeat Thought/Action/Action Input/Observation as needed)\n"
                + "Final Answer: your final answer to the user's question";
    }

    private boolean containsFinalAnswer(String text) {
        for (String line : text.split("\n")) {
            if (line.trim().startsWith(FINAL_ANSWER_PREFIX)) {
                return true;
            }
        }
        return false;
    }

    private String extractFinalAnswer(String text) {
        for (String line : text.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith(FINAL_ANSWER_PREFIX)) {
                return trimmed.substring(FINAL_ANSWER_PREFIX.length()).trim();
            }
        }
        return text;
    }

    private String extractLine(String text, String prefix) {
        for (String line : text.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith(prefix)) {
                return trimmed.substring(prefix.length()).trim();
            }
        }
        return null;
    }
}
