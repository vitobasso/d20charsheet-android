package com.vituel.dndplayer.activity.abstraction;

import android.view.View;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.effect.EffectSource;
import com.vituel.dndplayer.util.gui.EffectPopulator;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractSelectEffectActivity<T extends AbstractEntity & EffectSource> extends AbstractSelectActivity<T> {

    @Override
    protected int getRowLayout() {
        return R.layout.effect_row;
    }

    @Override
    protected void onPopulateRow(View view, T entity) {
        EffectPopulator populator = new EffectPopulator(this);
        populator.populate(entity, (android.view.ViewGroup) view);
    }
}
