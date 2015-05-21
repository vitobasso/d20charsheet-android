package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.util.ActivityUtil;

/**
 * Created by Victor on 29/09/2014.
 */
public class ConditionTranslator {

    private Context ctx;

    public ConditionTranslator(Context ctx) {
        this.ctx = ctx;
    }

    public String translate(String conditionName) {
        String resourceName = conditionName.replace(' ', '_');
        return ActivityUtil.getStringResource(ctx, resourceName, conditionName);
    }

}
