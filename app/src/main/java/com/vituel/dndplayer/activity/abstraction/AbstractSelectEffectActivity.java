package com.vituel.dndplayer.activity.abstraction;

import android.os.Bundle;

import com.vituel.dndplayer.activity.EffectArrayAdapter;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.EffectSource;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractSelectEffectActivity<T extends AbstractEntity & EffectSource> extends AbstractSelectActivity<T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView.setAdapter(new EffectArrayAdapter<>(this, list));
    }
}
