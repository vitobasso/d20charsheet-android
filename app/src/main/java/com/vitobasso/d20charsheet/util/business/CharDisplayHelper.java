package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;

import java.util.Locale;

/**
 * Created by Victor on 11/06/2015.
 */
public class CharDisplayHelper {

    private Context context;
    private CharBase base;
    private CharSummary summary;

    public CharDisplayHelper(Context context, CharBase base) {
        this.context = context;
        this.base = base;
    }

    public CharDisplayHelper(Context context, CharSummary summary) {
        this.context = context;
        this.summary = summary;
        this.base = summary.getBase();
    }

    public String getDescription() {
        String description = base.getDescription();
        return ActivityUtil.internationalize(description, context);
    }

    public String getSpeed() {
        return createDistanceHelper().getSpeed();
    }

    private DistanceHelper createDistanceHelper() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "pt":
                return new MeterDistanceHelper(context, summary);
            default:
                return new FeetDistanceHelper(context, summary);
        }
    }

}
