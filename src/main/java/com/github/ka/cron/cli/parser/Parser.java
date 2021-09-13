package com.github.ka.cron.cli.parser;

import com.github.ka.cron.cli.ParsingException;
import com.github.ka.cron.cli.Schedule;

import java.util.List;

/**
 * Basic cron parser.
 * Expects the input expression to have 6 fields: minute, hour, day of month, month, day of week, command.
 * Validate fields and throws {@link ParsingException}.
 *
 * Supported values:
 * minute: 0-59
 * hour: 0-23
 * day of month: 1-31
 * month: 1-12
 * day of week: 0-6
 * command: any string
 *
 * Parse doesn't support short name for day of week (Sun, Mon, Tue, etc) and month (Jan, Feb, etc).
 */
public class Parser {

    @FunctionalInterface
    interface UpdateField {
        Schedule update(Schedule schedule, String value);
    }

    static final List<UpdateField> FIELDS = List.of(
            Parser::parseMinutes,
            Parser::parseHour,
            Parser::parseDom,
            Parser::parseMonth,
            Parser::parseDow,
            Parser::parseCommand
    );

    public static Schedule parse(String[] fields) {
        var schedule = Schedule.builder().build();
        validateSizeOfFields(fields);

        int fieldStartAt = 0;
        try {
            for (int i = 0; i < fields.length; i++) {
                var ranges = fields[i].split(",");
                for (var range: ranges) {
                    schedule = FIELDS.get(i).update(schedule, range);
                    fieldStartAt += range.length();
                }
            }
        } catch (ParsingException e) {
            var updatedPosition = e.getInvalidCharPosition() != -1 ? e.getInvalidCharPosition() + fieldStartAt : -1;
            throw new ParsingException(e.getLocalizedMessage(), updatedPosition);
        }

        return schedule;
    }


    static void validateSizeOfFields(String[] fields) {
        if (fields == null || fields.length != FIELDS.size()) {
            var size = fields == null ? 0 : fields.length;
            var message = String.format("Expected %d fields \"min hour dayOfMonth month dayOfWeek command\" " +
                    "in cron expression but got %d", FIELDS.size(), size);
            throw new ParsingException(message);
        }
    }

    static Schedule parseMinutes(Schedule schedule, String value) {
        var minute = parseValue(value.trim(), 0, 59);
        return schedule.toBuilder()
                .minute(schedule.getMinute() | minute)
                .build();
    }

    static Schedule parseHour(Schedule schedule, String value) {
        var hour = parseValue(value.trim(), 0, 23);
        return schedule.toBuilder()
                .hour(schedule.getHour() | hour)
                .build();
    }

    static Schedule parseDom(Schedule schedule, String value) {
        var dom = parseValue(value.trim(), 1, 31);
        return schedule.toBuilder()
                .dayOfMonth(schedule.getDayOfMonth() | dom)
                .build();
    }

    static Schedule parseMonth(Schedule schedule, String value) {
        var month = parseValue(value.trim(), 1, 12);
        return schedule.toBuilder()
                .month(schedule.getMonth() | month)
                .build();
    }

    static Schedule parseDow(Schedule schedule, String value) {
        var dow = parseValue(value.trim(), 0, 6);
        return schedule.toBuilder()
                .dayOfWeek(schedule.getDayOfWeek() | dow)
                .build();
    }

    static Schedule parseCommand(Schedule schedule, String value) {
        return schedule.toBuilder()
                .command(value.trim())
                .build();
    }

    /*
        Accepts following formats:
        1-15
        *
        * /15
        1-60/15
     */
    static long parseValue(String value, int start, int end) {
        // split the string by / first then analyze the dividend
        var divisorIndex = value.indexOf('/');
        var dividend = divisorIndex != -1 ? value.substring(0, divisorIndex).trim() : value;
        if (dividend.length() == 0) {
            throw new ParsingException("expected a number or * but got /", 0);
        }
        var divisor = divisorIndex != -1 ? Integer.parseInt(value.substring(divisorIndex + 1)) : 1;

        long result = parseDividend(dividend, start, end);

        for (int i=start; i<=end; i++) {
            // if not dividable and the bit value set to 1 (e.g. */15 and i == 3) then set i-th bit to 0
            long mask = (1L << i);
            if (i % divisor != 0 && ((result & mask) == mask)) {
                result &= ~(1L << i);
            }
        }

        return result;
    }

    static long parseDividend(String value, int start, int end) {
        long result = 0;
        int actualStart = start;
        int actualEnd = end;

        if (value.contains("-")) {
            var index = value.indexOf('-');
            actualStart = Integer.parseInt(value.substring(0, index));
            actualEnd = Integer.parseInt(value.substring(index+1));
        } else if (!"*".equals(value)) {
            actualStart = Integer.parseInt(value);
            actualEnd = actualStart;
        }

        if (actualStart < start || actualStart > end) {
            throw new ParsingException(String.format("value should be between %d and %d but got %d", start, end, actualStart), 0);
        }
        if (actualEnd < start || actualEnd > end) {
            throw new ParsingException(String.format("value should be between %d and %d but got %d", start, end, actualEnd), 0);
        }

        for (int i=actualStart; i<=actualEnd; i++) {
            result |= 1L << i;
        }

        return result;
    }
}
