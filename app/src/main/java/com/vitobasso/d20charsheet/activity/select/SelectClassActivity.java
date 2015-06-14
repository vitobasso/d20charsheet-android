package com.vitobasso.d20charsheet.activity.select;

import com.vitobasso.d20charsheet.activity.abstraction.AbstractSelectActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.entity.ClassDao;
import com.vitobasso.d20charsheet.model.Clazz;

/**
 * Created by Victor on 28/02/14.
 */
public class SelectClassActivity extends AbstractSelectActivity<Clazz> {

    @Override
    protected AbstractEntityDao<Clazz> createDataSource() {
        return new ClassDao(this);
    }

}
