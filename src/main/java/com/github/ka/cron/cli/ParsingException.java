package com.github.ka.cron.cli;

import lombok.Getter;

@Getter
public class ParsingException extends RuntimeException {

    private final int invalidCharPosition;

    public ParsingException(String message) {
        this(message, -1);
    }

    public ParsingException(String message, int invalidCharPosition) {
        super(message);
        this.invalidCharPosition = invalidCharPosition;
    }
}
