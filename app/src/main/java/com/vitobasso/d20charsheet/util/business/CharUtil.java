package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;

/**
 * Created by Victor on 11/06/2015.
 */
public class CharUtil {

    private Context context;
    private CharBase charBase;

    public CharUtil(Context context, CharBase charBase) {
        this.context = context;
        this.charBase = charBase;
    }

    public String getDescription() {
        String description = charBase.getDescription();
        return ActivityUtil.internationalize(description, context);
    }

}
