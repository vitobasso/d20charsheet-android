package com.vitobasso.d20charsheet.activity.edit;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditEffectActivity;
import com.vitobasso.d20charsheet.model.TempEffect;

public class EditTempEffectActivity extends AbstractEditEffectActivity<TempEffect> {

    @Override
    protected int getLayout() {
        return R.layout.edit_temp_effect;
    }

}
