package com.zylophix.core.tool.builtin;

import com.zylophix.core.tool.Tool;
import com.zylophix.core.tool.ToolDefinition;
import com.zylophix.core.tool.ToolResult;

/**
 * A tool that evaluates basic arithmetic expressions (+, -, *, /, parentheses).
 *
 * <p>Input format: a mathematical expression string, e.g. {@code "2 + 3 * 4"}.
 */
public class CalculatorTool implements Tool {

    private static final ToolDefinition DEFINITION = new ToolDefinition(
            "calculator",
            "Evaluates a mathematical expression and returns the numeric result.",
            "A mathematical expression string (e.g. '2 + 3 * 4', '(10 / 2) - 1')."
    );

    @Override
    public ToolDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    public ToolResult execute(String input) {
        if (input == null || input.isBlank()) {
            return ToolResult.failure("Expression must not be empty.");
        }

        String sanitized = input.trim().replaceAll("\\s+", "");
        if (!sanitized.matches("[0-9+\\-*/().]+")) {
            return ToolResult.failure("Expression contains unsupported characters: " + sanitized);
        }

        try {
            double result = new ExpressionParser(sanitized).parse();
            // Return integer representation when the result has no fractional part
            if (result == Math.floor(result) && !Double.isInfinite(result)) {
                return ToolResult.success(String.valueOf((long) result));
            }
            return ToolResult.success(String.valueOf(result));
        } catch (ArithmeticException | IllegalArgumentException e) {
            return ToolResult.failure("Failed to evaluate expression: " + e.getMessage());
        }
    }

    /**
     * Recursive-descent parser for arithmetic expressions.
     * Supports: +, -, *, /, unary minus, and parentheses.
     */
    private static class ExpressionParser {
        private final String expr;
        private int pos;

        ExpressionParser(String expr) {
            this.expr = expr;
            this.pos = 0;
        }

        double parse() {
            double result = parseExpression();
            if (pos < expr.length()) {
                throw new IllegalArgumentException("Unexpected character at position " + pos + ": " + expr.charAt(pos));
            }
            return result;
        }

        // additive
        private double parseExpression() {
            double result = parseTerm();
            while (pos < expr.length()) {
                char op = expr.charAt(pos);
                if (op == '+') {
                    pos++;
                    result += parseTerm();
                } else if (op == '-') {
                    pos++;
                    result -= parseTerm();
                } else {
                    break;
                }
            }
            return result;
        }

        // multiplicative
        private double parseTerm() {
            double result = parseFactor();
            while (pos < expr.length()) {
                char op = expr.charAt(pos);
                if (op == '*') {
                    pos++;
                    result *= parseFactor();
                } else if (op == '/') {
                    pos++;
                    double divisor = parseFactor();
                    if (divisor == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    result /= divisor;
                } else {
                    break;
                }
            }
            return result;
        }

        // unary, parentheses, number
        private double parseFactor() {
            if (pos >= expr.length()) {
                throw new IllegalArgumentException("Unexpected end of expression");
            }
            char ch = expr.charAt(pos);
            if (ch == '-') {
                pos++;
                return -parseFactor();
            }
            if (ch == '+') {
                pos++;
                return parseFactor();
            }
            if (ch == '(') {
                pos++; // consume '('
                double result = parseExpression();
                if (pos >= expr.length() || expr.charAt(pos) != ')') {
                    throw new IllegalArgumentException("Missing closing parenthesis");
                }
                pos++; // consume ')'
                return result;
            }
            return parseNumber();
        }

        private double parseNumber() {
            int start = pos;
            while (pos < expr.length() && (Character.isDigit(expr.charAt(pos)) || expr.charAt(pos) == '.')) {
                pos++;
            }
            if (start == pos) {
                throw new IllegalArgumentException("Expected number at position " + pos);
            }
            return Double.parseDouble(expr.substring(start, pos));
        }
    }
}
