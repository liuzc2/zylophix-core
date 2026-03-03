package com.zylophix.core.llm;

/**
 * A single message in a conversation with an LLM.
 */
public class LLMMessage {

    public enum Role {
        SYSTEM, USER, ASSISTANT
    }

    private final Role role;
    private final String content;

    public LLMMessage(Role role, String content) {
        this.role = role;
        this.content = content;
    }

    public static LLMMessage system(String content) {
        return new LLMMessage(Role.SYSTEM, content);
    }

    public static LLMMessage user(String content) {
        return new LLMMessage(Role.USER, content);
    }

    public static LLMMessage assistant(String content) {
        return new LLMMessage(Role.ASSISTANT, content);
    }

    public Role getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return role.name().toLowerCase() + ": " + content;
    }
}
