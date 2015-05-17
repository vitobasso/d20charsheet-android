package com.vituel.dndplayer.activity.select;

import com.vituel.dndplayer.activity.abstraction.AbstractEditActivity;
import com.vituel.dndplayer.activity.abstraction.AbstractSelectActivity;
import com.vituel.dndplayer.activity.edit.EditSkillActivity;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.entity.SkillDao;
import com.vituel.dndplayer.model.Skill;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectSkillActivity extends AbstractSelectActivity<Skill> {

    @Override
    protected AbstractEntityDao<Skill> createDataSource() {
        return new SkillDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditSkillActivity.class;
    }
}
