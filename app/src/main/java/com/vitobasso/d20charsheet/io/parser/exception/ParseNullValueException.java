package com.vitobasso.d20charsheet.io.parser.exception;

/**
 * Created by Victor on 13/04/2015.
 */
public class ParseNullValueException extends ParseFieldException {

    public ParseNullValueException(int columnIndex) {
        this(columnIndex, null);
    }

    public ParseNullValueException(int columnIndex, Throwable throwable) {
        super(columnIndex, "Can't parse null value.", throwable);
    }

}
