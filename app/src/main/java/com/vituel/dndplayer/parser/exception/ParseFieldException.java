package com.vituel.dndplayer.parser.exception;

/**
 * Created by Victor on 13/04/2015.
 */
public class ParseFieldException extends ParseException {

    protected int columnIndex;

    public ParseFieldException(int columnIndex, String detailedMessage, Throwable throwable) {
        super(detailedMessage, throwable);
    }

    public ParseFieldException(int columnIndex, String detailedMessage) {
        this(columnIndex, detailedMessage, null);
    }

    public int getColumnIndex() {
        return columnIndex;
    }

}
