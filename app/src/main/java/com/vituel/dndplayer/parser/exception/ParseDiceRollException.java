package com.vituel.dndplayer.parser.exception;

/**
 * Created by Victor on 25/04/2015.
 */
public class ParseDiceRollException extends ParseFieldException {

    public ParseDiceRollException(int columnIndex, String detailedMessage, Throwable throwable) {
        super(columnIndex, detailedMessage, throwable);
    }

    public ParseDiceRollException(int columnIndex, String detailedMessage) {
        super(columnIndex, detailedMessage);
    }

    public ParseDiceRollException(String detailedMessage) {
        super(-1, detailedMessage); //TODO fix parse exceptions: on some contexts index is not known
    }

    public ParseDiceRollException(String detailedMessage, Throwable throwable) {
        super(-1, detailedMessage, throwable); //TODO fix parse exceptions: on some contexts index is not known
    }
}
