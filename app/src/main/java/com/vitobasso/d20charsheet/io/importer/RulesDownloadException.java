package com.vitobasso.d20charsheet.io.importer;

/**
 * Created by Victor on 04/06/2015.
 */
public class RulesDownloadException extends RuntimeException {

    public RulesDownloadException(String message) {
        super(message);
    }

    public RulesDownloadException(String message, Exception e) {
        super(message, e);
    }

    public RulesDownloadException(Throwable throwable) {
        super(throwable);
    }
}
