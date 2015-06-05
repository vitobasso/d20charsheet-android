package com.vituel.dndplayer.io.downloader.exception;

/**
 * Created by Victor on 04/06/2015.
 */
public class HttpStatusException extends RuntimeException {

    private int statusCode;

    public HttpStatusException(int statusCode) {
        super(String.valueOf(statusCode));
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
