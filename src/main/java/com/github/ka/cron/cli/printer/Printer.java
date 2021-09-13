package com.github.ka.cron.cli.printer;

import com.github.ka.cron.cli.Schedule;

/**
 * Something that can print schedule and errors.
 * There's only one implementation.
 * It can be extended to support json output, for example.
 */
public interface Printer {
    void print(Schedule schedule);
    void printError(String s);
}
