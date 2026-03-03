package com.zylophix.core.memory;

import com.zylophix.core.llm.LLMMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConversationMemoryTest {

    @Test
    void addAndRetrieveMessages() {
        ConversationMemory memory = new ConversationMemory();
        memory.add(LLMMessage.system("You are helpful."));
        memory.add(LLMMessage.user("Hello"));
        memory.add(LLMMessage.assistant("Hi there!"));

        assertEquals(3, memory.size());
        assertEquals(LLMMessage.Role.SYSTEM, memory.getMessages().get(0).getRole());
        assertEquals("Hello", memory.getMessages().get(1).getContent());
    }

    @Test
    void clearResetsMemory() {
        ConversationMemory memory = new ConversationMemory();
        memory.add(LLMMessage.user("test"));
        memory.clear();

        assertEquals(0, memory.size());
    }

    @Test
    void maxMessagesTrimsOldestNonSystemMessage() {
        ConversationMemory memory = new ConversationMemory(3);
        memory.add(LLMMessage.system("System prompt"));
        memory.add(LLMMessage.user("msg1"));
        memory.add(LLMMessage.user("msg2"));
        // Adding a 4th message should evict the oldest non-system message
        memory.add(LLMMessage.user("msg3"));

        assertEquals(3, memory.size());
        // System message should still be present
        assertEquals(LLMMessage.Role.SYSTEM, memory.getMessages().get(0).getRole());
        // First user message should have been evicted
        assertEquals("msg2", memory.getMessages().get(1).getContent());
        assertEquals("msg3", memory.getMessages().get(2).getContent());
    }

    @Test
    void unlimitedMemoryDoesNotTrim() {
        ConversationMemory memory = new ConversationMemory(); // unlimited
        for (int i = 0; i < 100; i++) {
            memory.add(LLMMessage.user("msg" + i));
        }
        assertEquals(100, memory.size());
    }

    @Test
    void getMessagesReturnsUnmodifiableList() {
        ConversationMemory memory = new ConversationMemory();
        memory.add(LLMMessage.user("hello"));

        assertThrows(UnsupportedOperationException.class, () ->
                memory.getMessages().add(LLMMessage.user("extra")));
    }
}
