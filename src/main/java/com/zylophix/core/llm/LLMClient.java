package com.zylophix.core.llm;

import java.util.List;

/**
 * Abstraction for a Large Language Model client.
 * Implementations can wrap OpenAI, Anthropic, local models, or any other LLM provider.
 */
public interface LLMClient {

    /**
     * Send a list of messages to the LLM and return its response.
     *
     * @param request the LLM request containing messages and parameters
     * @return the LLM response
     */
    LLMResponse chat(LLMRequest request);
}
