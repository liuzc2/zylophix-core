package com.zylophix.core.agent;

/**
 * Immutable configuration for an {@link Agent}.
 */
public class AgentConfig {

    private final String name;
    private final String systemPrompt;
    private final int maxIterations;

    private AgentConfig(Builder builder) {
        this.name = builder.name;
        this.systemPrompt = builder.systemPrompt;
        this.maxIterations = builder.maxIterations;
    }

    public String getName() {
        return name;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name = "ZylophixAgent";
        private String systemPrompt = "You are a helpful AI assistant.";
        private int maxIterations = 10;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder systemPrompt(String systemPrompt) {
            this.systemPrompt = systemPrompt;
            return this;
        }

        public Builder maxIterations(int maxIterations) {
            if (maxIterations < 1) {
                throw new IllegalArgumentException("maxIterations must be at least 1");
            }
            this.maxIterations = maxIterations;
            return this;
        }

        public AgentConfig build() {
            return new AgentConfig(this);
        }
    }
}
