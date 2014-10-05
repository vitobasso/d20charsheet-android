package com.vituel.dndplayer.activity;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectActivity;
import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.model.Clazz;

/**
 * Created by Victor on 28/02/14.
 */
public class SelectClassActivity extends AbstractSelectActivity<Clazz> {

    @Override
    protected AbstractEntityDao<Clazz> getDataSource() {
        return new ClassDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditClassActivity.class;
    }

}
