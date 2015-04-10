package com.vituel.dndplayer.activity;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectEffectActivity;
import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.dao.FeatDao;
import com.vituel.dndplayer.model.Feat;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectFeatActivity extends AbstractSelectEffectActivity<Feat> {

    @Override
    protected AbstractEntityDao<Feat> getDataSource() {
        return new FeatDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditFeatActivity.class;
    }

}
