package com.vitobasso.d20charsheet.activity.select;

import com.vitobasso.d20charsheet.activity.abstraction.AbstractSelectActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.entity.RaceDao;
import com.vitobasso.d20charsheet.model.Race;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectRaceActivity extends AbstractSelectActivity<Race> {

    @Override
    protected AbstractEntityDao<Race> createDataSource() {
        return new RaceDao(this);
    }

}
