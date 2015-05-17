package com.vituel.dndplayer.activity.select;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectActivity;
import com.vituel.dndplayer.activity.edit.EditClassActivity;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.entity.ClassDao;
import com.vituel.dndplayer.model.Clazz;

/**
 * Created by Victor on 28/02/14.
 */
public class SelectClassActivity extends AbstractSelectActivity<Clazz> {

    @Override
    protected AbstractEntityDao<Clazz> createDataSource() {
        return new ClassDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditClassActivity.class;
    }

}
