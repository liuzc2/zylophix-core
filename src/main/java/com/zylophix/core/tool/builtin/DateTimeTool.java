package com.zylophix.core.tool.builtin;

import com.zylophix.core.tool.Tool;
import com.zylophix.core.tool.ToolDefinition;
import com.zylophix.core.tool.ToolResult;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A tool that returns the current date and time in a specified timezone.
 *
 * <p>Input format: a timezone ID string (e.g. {@code "UTC"}, {@code "Asia/Shanghai"}),
 * or empty / blank to default to UTC.
 */
public class DateTimeTool implements Tool {

    private static final ToolDefinition DEFINITION = new ToolDefinition(
            "datetime",
            "Returns the current date and time for a given timezone.",
            "A timezone ID string such as 'UTC' or 'Asia/Shanghai'. Leave blank for UTC."
    );

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    @Override
    public ToolDefinition getDefinition() {
        return DEFINITION;
    }

    @Override
    public ToolResult execute(String input) {
        String zoneId = (input == null || input.isBlank()) ? "UTC" : input.trim();
        try {
            ZoneId zone = ZoneId.of(zoneId);
            ZonedDateTime now = ZonedDateTime.now(zone);
            return ToolResult.success(now.format(FORMATTER));
        } catch (Exception e) {
            return ToolResult.failure("Unknown timezone: " + zoneId);
        }
    }
}
