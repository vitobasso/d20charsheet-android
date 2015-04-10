package com.vituel.dndplayer.activity.abstraction;

import android.widget.ListAdapter;

import com.vituel.dndplayer.activity.EffectArrayAdapter;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.EffectSource;

import java.util.List;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractSelectEffectActivity<T extends AbstractEntity & EffectSource> extends AbstractSelectActivity<T> {

    @Override
    protected ListAdapter createAdapter(List<T> list) {
        return new EffectArrayAdapter<>(this, filteredList);
    }
}
