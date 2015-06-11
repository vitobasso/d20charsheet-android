package com.vitobasso.d20charsheet.io.char_io;

/**
 * Created by Victor on 11/06/2015.
 */
public class ExportCharException extends RuntimeException {

    public ExportCharException(String detailMessage) {
        super(detailMessage);
    }

    public ExportCharException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
