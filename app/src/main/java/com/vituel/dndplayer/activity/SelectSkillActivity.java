package com.vituel.dndplayer.activity;

import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.model.Skill;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectSkillActivity extends AbstractSelectActivity<Skill> {

    @Override
    protected AbstractEntityDao<Skill> getDataSource() {
        return new SkillDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditSkillActivity.class;
    }
}
