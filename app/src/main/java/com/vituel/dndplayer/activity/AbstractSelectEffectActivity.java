package com.vituel.dndplayer.activity;

import android.os.Bundle;

import com.vituel.dndplayer.model.AbstractEffect;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractSelectEffectActivity<T extends AbstractEffect> extends AbstractSelectActivity<T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView.setAdapter(new EffectArrayAdapter<>(this, list));
    }
}
