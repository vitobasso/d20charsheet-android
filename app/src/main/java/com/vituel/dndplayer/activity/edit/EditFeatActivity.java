package com.vituel.dndplayer.activity.edit;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditEffectActivity;
import com.vituel.dndplayer.model.Feat;

/**
 * Created by Victor on 30/03/14.
 */
public class EditFeatActivity extends AbstractEditEffectActivity<Feat> {

    @Override
    protected int getLayout() {
        return R.layout.edit_trait;
    }

}
