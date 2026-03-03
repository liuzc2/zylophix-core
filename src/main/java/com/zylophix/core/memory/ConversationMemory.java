package com.zylophix.core.memory;

import com.zylophix.core.llm.LLMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the conversation history between the user and the agent.
 * Provides access to the full message history for LLM context.
 */
public class ConversationMemory {

    private final List<LLMMessage> messages = new ArrayList<>();
    private final int maxMessages;

    /**
     * Create a ConversationMemory with an optional cap on history size.
     * When the cap is exceeded the oldest non-system messages are removed first.
     *
     * @param maxMessages maximum number of messages to keep (0 = unlimited)
     */
    public ConversationMemory(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public ConversationMemory() {
        this(0);
    }

    /**
     * Add a message to memory.
     */
    public synchronized void add(LLMMessage message) {
        messages.add(message);
        trimIfNeeded();
    }

    /**
     * Return an unmodifiable snapshot of all messages.
     */
    public synchronized List<LLMMessage> getMessages() {
        return Collections.unmodifiableList(new ArrayList<>(messages));
    }

    /**
     * Clear all messages from memory.
     */
    public synchronized void clear() {
        messages.clear();
    }

    /**
     * Return the number of messages currently in memory.
     */
    public synchronized int size() {
        return messages.size();
    }

    private void trimIfNeeded() {
        if (maxMessages > 0) {
            while (messages.size() > maxMessages) {
                // Remove the first non-system message to preserve the system prompt
                int removeIdx = 0;
                for (int i = 0; i < messages.size(); i++) {
                    if (messages.get(i).getRole() != LLMMessage.Role.SYSTEM) {
                        removeIdx = i;
                        break;
                    }
                }
                messages.remove(removeIdx);
            }
        }
    }
}
