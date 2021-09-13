package com.github.ka.cron.cli.printer;

import com.github.ka.cron.cli.Schedule;

public class TablePrinter implements Printer {

    @Override
    public void print(Schedule schedule) {
        System.out.println("table " + schedule);
    }

    @Override
    public void printError(String errorMessage) {
        System.err.println("error " + errorMessage);
    }
}
