package com.vituel.dndplayer.activity.abstraction;

import android.os.Bundle;

import com.vituel.dndplayer.activity.EffectArrayAdapter;
import com.vituel.dndplayer.model.AbstractEntity;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractSelectEffectActivity<T extends AbstractEntity> extends AbstractSelectActivity<T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView.setAdapter(new EffectArrayAdapter<>(this, list));
    }
}
