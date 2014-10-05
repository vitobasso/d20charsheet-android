package com.vituel.dndplayer.activity;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectActivity;
import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.model.Race;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectRaceActivity extends AbstractSelectActivity<Race> {

    @Override
    protected AbstractEntityDao<Race> getDataSource() {
        return new RaceDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditRaceActivity.class;
    }
}
