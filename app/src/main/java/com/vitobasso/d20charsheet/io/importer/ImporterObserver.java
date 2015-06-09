package com.vitobasso.d20charsheet.io.importer;

/**
 * Created by Victor on 21/04/2015.
 */
public interface ImporterObserver {

    void onBeginFile(String fileName);

    void onFinishBatch(int count);

}
