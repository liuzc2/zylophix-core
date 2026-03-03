package com.zylophix.core.agent;

/**
 * Core Agent interface. An agent perceives its environment, makes decisions,
 * and takes actions to achieve a goal using an LLM and a set of tools.
 */
public interface Agent {

    /**
     * Run the agent with the given user query and return the final result.
     *
     * @param query the user's input question or task
     * @return the agent's final answer
     */
    AgentResult run(String query);

    /**
     * Return the name of this agent.
     */
    String getName();
}
