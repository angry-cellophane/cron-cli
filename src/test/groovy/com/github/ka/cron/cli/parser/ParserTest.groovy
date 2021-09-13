package com.github.ka.cron.cli.parser

import com.github.ka.cron.cli.ParsingException
import com.github.ka.cron.cli.Schedule
import spock.lang.Specification
import spock.lang.Unroll

import static com.github.ka.cron.cli.BitUtils.bits
import static com.github.ka.cron.cli.BitUtils.setBitRange

class ParserTest extends Specification {

    @Unroll
    void 'parse values #name'() {
        expect:
        Parser.parseValue(value, start, end) == expected

        where:
        value   | start | end | expected             | name
        '42'    | 1     | 60  | setBits(42)          | 'set single value'
        '*'     | 1     | 60  | setBitRange(1, 60)   | 'set all values between 1 and 60'
        '*/2'   | 3     | 10  | setBits(4, 6, 8, 10) | 'set every second bit'
        '3-5'   | 3     | 10  | setBitRange(3, 5)    | 'set bit range'
        '3-8/2' | 1     | 20  | setBits(4, 6, 8)     | 'set every second bit in a range'
    }

    @Unroll
    void 'parse values throws error when #name'() {
        when:
        Parser.parseValue(value, start, end)

        then:
        thrown(ParsingException)

        where:
        value  | start | end | name
        '10'   | 1     | 2   | 'single value greater than boundaries'
        '0'    | 1     | 2   | 'single value smaller than boundaries'
        '0-3'  | 1     | 5   | 'range smaller than boundaries'
        '3-10' | 1     | 5   | 'range greater than boundaries'
    }

    @Unroll
    void 'parse minute #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        schedule = Parser.parseMinutes(schedule, value)

        then:
        schedule.minute == expected

        where:
        value   | expected               | name
        '42'    | setBits(42)            | 'set single value'
        '*'     | setBitRange(0, 59)     | 'set all values'
        '*/15'  | setBits(0, 15, 30, 45) | 'set every 15th minute'
        '3-5'   | setBitRange(3, 5)      | 'set range'
        '3-8/2' | setBits(4, 6, 8)       | 'set every second bit in a range'
    }

    @Unroll
    void 'parse minute throws error when #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        Parser.parseMinutes(schedule, value)

        then:
        thrown(ParsingException)

        where:
        value  | name
        '60'   | 'single value greater than boundaries'
        '3-70' | 'range greater than boundaries'
    }

    @Unroll
    void 'parse hour #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        schedule = Parser.parseHour(schedule, value)

        then:
        schedule.hour == expected

        where:
        value   | expected           | name
        '15'    | setBits(15)        | 'set single value'
        '*'     | setBitRange(0, 23) | 'set all values'
        '*/10'  | setBits(0, 10, 20) | 'set every 10th hour'
        '3-5'   | setBitRange(3, 5)  | 'set range'
        '3-8/2' | setBits(4, 6, 8)   | 'set every second bit in a range'
    }

    @Unroll
    void 'parse hour throws error when #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        Parser.parseHour(schedule, value)

        then:
        thrown(ParsingException)

        where:
        value  | name
        '24'   | 'single value greater than boundaries'
        '3-70' | 'range greater than boundaries'
    }


    @Unroll
    void 'parse dom #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        schedule = Parser.parseDom(schedule, value)

        then:
        schedule.dayOfMonth == expected

        where:
        value   | expected            | name
        '15'    | setBits(15)         | 'set single value'
        '*'     | setBitRange(1, 31)  | 'set all values'
        '*/10'  | setBits(10, 20, 30) | 'set every 10th day'
        '3-5'   | setBitRange(3, 5)   | 'set range'
        '3-8/2' | setBits(4, 6, 8)    | 'set every second bit in a range'
    }

    @Unroll
    void 'parse dom throws error when #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        Parser.parseDom(schedule, value)

        then:
        thrown(ParsingException)

        where:
        value  | name
        '32'   | 'single value greater than boundaries'
        '0'    | 'single value smaller than boundaries'
        '3-70' | 'range greater than boundaries'
        '0-13' | 'range smaller than boundaries'
    }

    @Unroll
    void 'parse month #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        schedule = Parser.parseMonth(schedule, value)

        then:
        schedule.month == expected

        where:
        value   | expected           | name
        '10'    | setBits(10)        | 'set single value'
        '*'     | setBitRange(1, 12) | 'set all values'
        '*/5'   | setBits(5, 10)     | 'set every 5th month'
        '3-5'   | setBitRange(3, 5)  | 'set range'
        '3-8/2' | setBits(4, 6, 8)   | 'set every second bit in a range'
    }

    @Unroll
    void 'parse month throws error when #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        Parser.parseMonth(schedule, value)

        then:
        thrown(ParsingException)

        where:
        value  | name
        '13'   | 'single value greater than boundaries'
        '0'    | 'single value smaller than boundaries'
        '3-70' | 'range greater than boundaries'
        '0-5'  | 'range smaller than boundaries'
    }


    @Unroll
    void 'parse dow #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        schedule = Parser.parseDow(schedule, value)

        then:
        schedule.dayOfWeek == expected

        where:
        value   | expected            | name
        '5'     | setBits(5)          | 'set single value'
        '*'     | setBitRange(0, 6)   | 'set all values'
        '*/2'   | setBits(0, 2, 4, 6) | 'set every 2nd day'
        '1-5'   | setBitRange(1, 5)   | 'set range'
        '2-5/2' | setBits(2, 4)       | 'set every second bit in a range'
    }

    @Unroll
    void 'parse dow throws error when #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        Parser.parseDow(schedule, value)

        then:
        thrown(ParsingException)

        where:
        value  | name
        '7'    | 'single value greater than boundaries'
        '3-10' | 'range greater than boundaries'
    }

    @Unroll
    void 'parse command #name'() {
        given:
        def schedule = Schedule.builder().build()

        when:
        schedule = Parser.parseCommand(schedule, value)

        then:
        schedule.command == expected

        where:
        value              | expected       | name
        '/usr/command'     | '/usr/command' | 'set single value'
        ' /usr/command   ' | '/usr/command' | 'trailing spaces deleted'
    }

    void 'parse expression'() {
        given:
        def expected = Schedule.builder()
                .minute(setBits(1, 2))
                .hour(setBits(3, 4))
                .dayOfMonth(setBits(5, 6))
                .month(setBits(7, 9, 10))
                .dayOfWeek(setBits(0, 3, 5))
                .command("/usr/command")
                .build()
        def expression = '1,2 3,4 5,6 7,9,10 0,3,5 /usr/command'

        when:
        def schedule = Parser.parse(expression)

        then:
        schedule == expected
    }
}
