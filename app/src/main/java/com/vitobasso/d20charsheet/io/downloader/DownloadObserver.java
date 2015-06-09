package com.vitobasso.d20charsheet.io.downloader;

/**
 * Created by Victor on 04/06/2015.
 */
public interface DownloadObserver {

    public enum Phase {
        CLEAN, DOWNLOAD, EXTRACT
    }

    void onBeginPhase(Phase name);

    void onProgress(long progress, long totalBytes);

}
