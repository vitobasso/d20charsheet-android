package com.vituel.dndplayer.util.gui;

/**
 * Created by Victor on 21/04/2015.
 */
public interface LoaderObserver {

    void onStartLoadingTable(String className);

    void onFinishLoadingEntity(String name, int count);

}
