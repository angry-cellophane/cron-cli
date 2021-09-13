package com.github.ka.cron.cli

import spock.lang.Specification
import spock.lang.Unroll

class AppTest extends Specification {

    @Unroll
    void 'error when args #caseName'() {
        when:
        App.validateArgs(args)

        then:
        thrown(ParsingException)

        where:
        args                   | caseName
        [] as String[]         | 'empty'
        null                   | 'null'
    }

    void 'point at char where error occurred'() {
        given:
        def expression = '0-3 b/2'
        def error = new ParsingException("unsupported char", 4)

        when:
        def message = App.prettifyError(expression, error)

        then:
        message == '''Error found at:
0-3 b/2
    ^

unsupported char
'''
    }
}
