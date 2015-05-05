package com.vituel.dndplayer.activity.select;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectEffectActivity;
import com.vituel.dndplayer.activity.edit.EditTempEffectActivity;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.entity.TempEffectDao;
import com.vituel.dndplayer.model.TempEffect;

/**
 * Created by Victor on 17/03/14.
 */
public class SelectTempEffectActivity extends AbstractSelectEffectActivity<TempEffect> {

    @Override
    protected AbstractEntityDao<TempEffect> getDataSource() {
        return new TempEffectDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditTempEffectActivity.class;
    }
}
