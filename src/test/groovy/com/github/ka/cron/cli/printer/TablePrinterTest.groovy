package com.github.ka.cron.cli.printer


import com.github.ka.cron.cli.Schedule
import spock.lang.Specification

import static com.github.ka.cron.cli.BitUtils.bits

class TablePrinterTest extends Specification {

    void 'print sample expression'() {
        given:
        def expected = '''minute        1 2
hour          3 4
day of month  5 6
month         7 9 10
day of week   0 3 5
command       /usr/command
'''

        def schedule = Schedule.builder()
                .minute(setBits(1, 2))
                .hour(setBits(3, 4))
                .dayOfMonth(setBits(5, 6))
                .month(setBits(7, 9, 10))
                .dayOfWeek(setBits(0, 3, 5))
                .command("/usr/command")
                .build()
        def stdout = new ByteArrayOutputStream()
        def stderr = new ByteArrayOutputStream()
        def printer = new TablePrinter(new PrintStream(stdout, true), new PrintStream(stderr, true))

        when:
        printer.print(schedule)

        then:
        stderr.size() == 0
        stdout.toString() == expected
    }

    void 'print error'() {
        given:
        def expected = 'Failed with error\nerror\n'

        def stdout = new ByteArrayOutputStream()
        def stderr = new ByteArrayOutputStream()
        def printer = new TablePrinter(new PrintStream(stdout, true), new PrintStream(stderr, true))

        when:
        printer.printError('error')

        then:
        stdout.size() == 0
        stderr.toString() == expected
    }
}
