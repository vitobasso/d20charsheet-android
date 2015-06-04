package com.vituel.dndplayer.io.parser.exception;

/**
 * Created by Victor on 25/04/2015.
 */
public class ParseModifierException extends ParseFieldException {

    public ParseModifierException(int columnIndex, String detailedMessage, Throwable throwable) {
        super(columnIndex, detailedMessage, throwable);
    }

    public ParseModifierException(int columnIndex, String detailedMessage) {
        super(columnIndex, detailedMessage);
    }

    public ParseModifierException(String detailedMessage) {
        super(-1, detailedMessage); //TODO fix parse exceptions: on some contexts index is not known
    }

    public ParseModifierException(String detailedMessage, Throwable throwable) {
        super(-1, detailedMessage, throwable); //TODO fix parse exceptions: on some contexts index is not known
    }
}
