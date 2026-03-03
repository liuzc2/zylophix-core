package com.zylophix.core.llm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A request to an LLM, containing the conversation messages and optional parameters.
 */
public class LLMRequest {

    private final List<LLMMessage> messages;
    private final double temperature;
    private final int maxTokens;

    private LLMRequest(Builder builder) {
        this.messages = Collections.unmodifiableList(new ArrayList<>(builder.messages));
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
    }

    public List<LLMMessage> getMessages() {
        return messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<LLMMessage> messages = new ArrayList<>();
        private double temperature = 0.7;
        private int maxTokens = 2048;

        public Builder messages(List<LLMMessage> messages) {
            this.messages = new ArrayList<>(messages);
            return this;
        }

        public Builder addMessage(LLMMessage message) {
            this.messages.add(message);
            return this;
        }

        public Builder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public LLMRequest build() {
            return new LLMRequest(this);
        }
    }
}
