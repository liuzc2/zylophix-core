package com.zylophix.core.agent;

/**
 * Represents the final result produced by an {@link Agent} after completing its reasoning loop.
 */
public class AgentResult {

    private final String answer;
    private final boolean success;
    private final int iterationsUsed;
    private final String errorMessage;

    private AgentResult(String answer, boolean success, int iterationsUsed, String errorMessage) {
        this.answer = answer;
        this.success = success;
        this.iterationsUsed = iterationsUsed;
        this.errorMessage = errorMessage;
    }

    public static AgentResult success(String answer, int iterationsUsed) {
        return new AgentResult(answer, true, iterationsUsed, null);
    }

    public static AgentResult failure(String errorMessage, int iterationsUsed) {
        return new AgentResult(null, false, iterationsUsed, errorMessage);
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getIterationsUsed() {
        return iterationsUsed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (success) {
            return "AgentResult{success=true, answer='" + answer + "', iterations=" + iterationsUsed + "}";
        }
        return "AgentResult{success=false, error='" + errorMessage + "', iterations=" + iterationsUsed + "}";
    }
}
