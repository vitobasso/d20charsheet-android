package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.model.character.CharSummary;

/**
 * Created by Victor on 27/06/2015.
 */
public class MeterDistanceHelper extends DistanceHelper {

    public MeterDistanceHelper(Context context, CharSummary summary) {
        super(context, summary);
    }

    @Override
    protected float getUnitsPerSquare() {
        return 1.5f;
    }

}
