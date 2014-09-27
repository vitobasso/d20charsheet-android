package com.vituel.dndplayer.activity;

import com.vituel.dndplayer.dao.AbstractEntityDao;
import com.vituel.dndplayer.dao.TraitDao;
import com.vituel.dndplayer.model.Trait;

import java.util.List;

import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_TYPE;

/**
 * Created by Victor on 30/03/14.
 */
public class SelectTraitActivity extends AbstractSelectEffectActivity<Trait> {

    Trait.Type traitType;

    @Override
    protected AbstractEntityDao<Trait> getDataSource() {
        return new TraitDao(this);
    }

    @Override
    protected Class<? extends AbstractEditActivity> getEditActivityClass() {
        return EditTraitActivity.class;
    }

    @Override
    protected void onPrePopulate() {
        traitType = (Trait.Type) getIntent().getSerializableExtra(EXTRA_TYPE);
    }

    @Override
    protected List<Trait> onQueryDB(AbstractEntityDao<Trait> dataSource) {
        if (traitType != null) {
            return ((TraitDao)dataSource).findByType(traitType);
        } else {
            return dataSource.listAll();
        }
    }
}
