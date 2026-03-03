package com.zylophix.core.llm;

/**
 * The response returned by an LLM after processing a request.
 */
public class LLMResponse {

    private final String content;
    private final int promptTokens;
    private final int completionTokens;

    public LLMResponse(String content, int promptTokens, int completionTokens) {
        this.content = content;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
    }

    public static LLMResponse of(String content) {
        return new LLMResponse(content, 0, 0);
    }

    public String getContent() {
        return content;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public int getTotalTokens() {
        return promptTokens + completionTokens;
    }

    @Override
    public String toString() {
        return "LLMResponse{content='" + content + "', totalTokens=" + getTotalTokens() + "}";
    }
}
