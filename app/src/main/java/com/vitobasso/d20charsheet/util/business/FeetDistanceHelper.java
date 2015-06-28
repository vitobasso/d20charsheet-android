package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.model.character.CharSummary;

/**
 * Created by Victor on 27/06/2015.
 */
public class FeetDistanceHelper extends DistanceHelper {

    public FeetDistanceHelper(Context context, CharSummary summary) {
        super(context, summary);
    }

    @Override
    protected float getUnitsPerSquare() {
        return 5f;
    }

}
