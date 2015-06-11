package com.vitobasso.d20charsheet.io.char_io;

/**
 * Created by Victor on 11/06/2015.
 */
public class ImportCharException extends RuntimeException {

    public ImportCharException(String detailMessage) {
        super(detailMessage);
    }

    public ImportCharException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
