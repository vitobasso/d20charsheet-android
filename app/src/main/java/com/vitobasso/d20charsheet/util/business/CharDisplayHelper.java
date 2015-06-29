package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;

/**
 * Created by Victor on 11/06/2015.
 */
public class CharDisplayHelper {

    private Context context;
    private CharBase base;
    private CharSummary summary;
    private DistanceHelper distanceHelper;

    public CharDisplayHelper(Context context, CharBase base) {
        this.context = context;
        this.base = base;
        this.distanceHelper = new DistanceHelper(context);
    }

    public CharDisplayHelper(Context context, CharSummary summary) {
        this(context, summary.getBase());
        this.summary = summary;
    }

    public String getDescription() {
        String description = base.getDescription();
        return ActivityUtil.internationalize(description, context);
    }

    public String getSpeed() {
        int speedInSquares = summary.getSpeed();
        return distanceHelper.getSpeedAsString(speedInSquares);
    }

}
