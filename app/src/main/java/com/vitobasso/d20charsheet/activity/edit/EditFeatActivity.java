package com.vitobasso.d20charsheet.activity.edit;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.AbstractEditEffectActivity;
import com.vitobasso.d20charsheet.model.Feat;

/**
 * Created by Victor on 30/03/14.
 */
public class EditFeatActivity extends AbstractEditEffectActivity<Feat> {

    @Override
    protected int getLayout() {
        return R.layout.edit_trait;
    }

}
