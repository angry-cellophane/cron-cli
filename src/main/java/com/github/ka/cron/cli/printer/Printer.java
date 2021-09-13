package com.github.ka.cron.cli.printer;

import com.github.ka.cron.cli.Schedule;

public interface Printer {
    void print(Schedule schedule);
    void printError(String s);
}
