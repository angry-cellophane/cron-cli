package com.github.ka.cron.cli;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Uses bitmask to store up to 64 values.
 * e.g. minute stores values from 0 to 60 where i-th bit represents if the cron job runs on the i-th minute.
 * 0 == not running, 1 == running.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Schedule {
    long minute;
    long second;
    long hour;
    long dayOfMonth;
    long month;
    long dayOfWeek;
    String command;
}
