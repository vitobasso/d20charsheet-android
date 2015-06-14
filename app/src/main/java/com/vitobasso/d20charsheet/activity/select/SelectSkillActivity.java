package com.vitobasso.d20charsheet.activity.select;

import com.vitobasso.d20charsheet.activity.abstraction.AbstractSelectActivity;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.entity.SkillDao;
import com.vitobasso.d20charsheet.model.Skill;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectSkillActivity extends AbstractSelectActivity<Skill> {

    @Override
    protected AbstractEntityDao<Skill> createDataSource() {
        return new SkillDao(this);
    }

}
