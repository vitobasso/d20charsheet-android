package com.vituel.dndplayer.activity.select;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectActivity;
import com.vituel.dndplayer.activity.edit.EditRaceActivity;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.entity.RaceDao;
import com.vituel.dndplayer.model.Race;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectRaceActivity extends AbstractSelectActivity<Race> {

    @Override
    protected AbstractEntityDao<Race> createDataSource() {
        return new RaceDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditRaceActivity.class;
    }
}
