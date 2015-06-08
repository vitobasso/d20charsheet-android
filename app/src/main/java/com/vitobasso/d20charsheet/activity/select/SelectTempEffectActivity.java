package com.vitobasso.d20charsheet.activity.select;

import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditActivity;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractSelectEffectActivity;
import com.vitobasso.d20charsheet.activity.edit.EditTempEffectActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.entity.TempEffectDao;
import com.vitobasso.d20charsheet.model.TempEffect;

/**
 * Created by Victor on 17/03/14.
 */
public class SelectTempEffectActivity extends AbstractSelectEffectActivity<TempEffect> {

    @Override
    protected AbstractEntityDao<TempEffect> createDataSource() {
        return new TempEffectDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditTempEffectActivity.class;
    }
}
