package com.vituel.dndplayer.activity.edit;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.AbstractEditEffectActivity;
import com.vituel.dndplayer.model.TempEffect;

public class EditTempEffectActivity extends AbstractEditEffectActivity<TempEffect> {

    @Override
    protected int getLayout() {
        return R.layout.edit_temp_effect;
    }

}
