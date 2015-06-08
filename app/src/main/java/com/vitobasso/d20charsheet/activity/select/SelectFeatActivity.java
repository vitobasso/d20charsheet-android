package com.vitobasso.d20charsheet.activity.select;

import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditActivity;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractSelectEffectActivity;
import com.vitobasso.d20charsheet.activity.edit.EditFeatActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.entity.FeatDao;
import com.vitobasso.d20charsheet.model.Feat;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectFeatActivity extends AbstractSelectEffectActivity<Feat> {

    @Override
    protected AbstractEntityDao<Feat> createDataSource() {
        return new FeatDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditFeatActivity.class;
    }

}
