package com.vituel.dndplayer.util.gui;

/**
 * Created by Victor on 21/04/2015.
 */
public interface LoaderObserver {

    void onStartLoadingFile(String fileName);

    void onFinishLoadingRow(String name, int count);

}
