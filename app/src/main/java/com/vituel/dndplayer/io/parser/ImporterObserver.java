package com.vituel.dndplayer.io.parser;

/**
 * Created by Victor on 21/04/2015.
 */
public interface ImporterObserver {

    void onStartFile(String fileName);

    void onFinishBatch(int count);

}
