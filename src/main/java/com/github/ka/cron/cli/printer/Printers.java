package com.github.ka.cron.cli.printer;

public class Printers {
    public static Printer from(String[] args) {
        return new TablePrinter(System.out, System.err);
    }
}
