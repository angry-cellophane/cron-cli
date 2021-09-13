package com.github.ka.cron.cli.printer;

import com.github.ka.cron.cli.Schedule;
import lombok.AllArgsConstructor;

import java.io.PrintStream;

@AllArgsConstructor
public class TablePrinter implements Printer {

    private final PrintStream stdout;
    private final PrintStream stderr;

    @Override
    public void print(Schedule schedule) {
        stdout.printf(
                "minute        %s\n" +
                "hour          %s\n" +
                "day of month  %s\n" +
                "month         %s\n" +
                "day of week   %s\n" +
                "command       %s\n",
                getValues(schedule.getMinute(), 59),
                getValues(schedule.getHour(), 23),
                getValues(schedule.getDayOfMonth(), 31),
                getValues(schedule.getMonth(), 12),
                getValues(schedule.getDayOfWeek(), 6),
                schedule.getCommand()
        );
    }

    @Override
    public void printError(String errorMessage) {
        stderr.println("Failed with error\n" + errorMessage);
    }

    static String getValues(long values, int end) {
        var output = new StringBuilder();
        var isFirstValue = true;
        for (int i=0; i<=end; i++) {
            long mask = (1L << i);
            if ((values & mask) != mask) continue; // continue if value is not set

            if (isFirstValue) {
                isFirstValue = false;
            } else {
                output.append(' ');
            }

            output.append(i);
        }
        return output.toString();
    }
}
