package com.vitobasso.d20charsheet.activity.abstraction;

import android.view.View;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.effect.EffectSource;
import com.vitobasso.d20charsheet.util.gui.EffectPopulator;

/**
 * Created by Victor on 21/04/14.
 */
public abstract class AbstractSelectEffectActivity<T extends AbstractEntity & EffectSource> extends AbstractSelectEditActivity<T> {

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
