package com.github.ka.cron.cli;

import com.github.ka.cron.cli.parser.Parser;
import com.github.ka.cron.cli.printer.Printers;

public class App {

    public static void main(String[] args) {
        var printer = Printers.from(args);
        try {
            validateArgs(args);
            var expression = String.join(" ", args);

            printer.print(Parser.parse(expression));
        } catch (ParsingException e) {
            printer.printError(prettifyError(String.join(" ", args), e));
            System.exit(1);
        } catch (Exception e) {
            // TODO create a file with error log, ask user to submit the log
            printer.printError("Unexpected error");
            System.exit(2);
        }
    }

    static void validateArgs(String[] args) {
        if (args == null || args.length == 0) {
            throw new ParsingException("Expected cron expression in arguments but got nothing." + USAGE);
        }
    }

    static String prettifyError(String expression, ParsingException e) {
        if (expression == null || expression.length() == 0) return e.getLocalizedMessage();

        var newMessage = new StringBuilder()
                .append("Error found at:\n")
                .append(expression).append('\n');
        for (int i=0; i<e.getInvalidCharPosition(); i++) {
            newMessage.append(' ');
        }
        newMessage.append('^');
        newMessage.append('\n').append('\n');
        newMessage.append(e.getLocalizedMessage());
        newMessage.append('\n');

        return newMessage.toString();
    }


    static final String USAGE = "\nUsage:\ncron-cli \"*/15 0 1,15 * 1-5 /usr/bin/find\"";
}
