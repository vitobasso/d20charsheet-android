package com.vituel.dndplayer.io.downloader.exception;

/**
 * Created by Victor on 04/06/2015.
 */
public class CreateFileException extends RuntimeException {

    private String path;

    public CreateFileException(String path) {
        super(path);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
