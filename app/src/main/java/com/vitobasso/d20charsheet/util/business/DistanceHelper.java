package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.character.CharSummary;

/**
 * Created by Victor on 27/06/2015.
 */
public abstract class DistanceHelper {

    private Context context;
    private CharSummary summary;

    public DistanceHelper(Context context, CharSummary summary) {
        this.context = context;
        this.summary = summary;
    }

    public String getSpeed() {
        int squaresValue = summary.getSpeed();
        float displayValue = convertToDisplayUnit(squaresValue);
        return String.format("%.0f %s", displayValue, getUnitAbbreviation());
    }

    private float convertToDisplayUnit(int valueInSquares) {
        return valueInSquares * getUnitsPerSquare();
    }

    private int distanceInFeet(int squares) {
        return squares * 5;
    }

    protected abstract float getUnitsPerSquare();

    protected String getUnitAbbreviation() {
        return context.getString(R.string.distance_unit_abbrev);
    }

}
