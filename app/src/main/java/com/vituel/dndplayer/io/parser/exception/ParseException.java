package com.vituel.dndplayer.io.parser.exception;

/**
 * Created by Victor on 13/04/2015.
 */
public abstract class ParseException extends Exception {

    public ParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseException(String detailMessage) {
        super(detailMessage);
    }
}
