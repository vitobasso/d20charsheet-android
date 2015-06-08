package com.vitobasso.d20charsheet.io.downloader;

/**
 * Created by Victor on 04/06/2015.
 */
public interface DownloadObserver {

    void onBeginStage(String name);

    void onProgress(int progress);

}
