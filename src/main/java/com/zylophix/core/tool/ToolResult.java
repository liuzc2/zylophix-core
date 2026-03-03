package com.zylophix.core.tool;

/**
 * Represents the outcome of invoking a {@link Tool}.
 */
public class ToolResult {

    private final boolean success;
    private final String output;
    private final String errorMessage;

    private ToolResult(boolean success, String output, String errorMessage) {
        this.success = success;
        this.output = output;
        this.errorMessage = errorMessage;
    }

    public static ToolResult success(String output) {
        return new ToolResult(true, output, null);
    }

    public static ToolResult failure(String errorMessage) {
        return new ToolResult(false, null, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getOutput() {
        return output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Returns the output on success or the error message on failure, suitable for feeding back to the LLM.
     */
    public String toObservation() {
        return success ? output : "Error: " + errorMessage;
    }

    @Override
    public String toString() {
        return success
                ? "ToolResult{success=true, output='" + output + "'}"
                : "ToolResult{success=false, error='" + errorMessage + "'}";
    }
}
