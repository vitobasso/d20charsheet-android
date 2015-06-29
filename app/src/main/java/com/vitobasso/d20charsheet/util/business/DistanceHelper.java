package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.R;

import java.util.Locale;

/**
 * Created by Victor on 27/06/2015.
 */
public class DistanceHelper {

    private Context context;

    public DistanceHelper(Context context) {
        this.context = context;
    }

    public String getAsString(int valueInSquares) {
        float displayValue = convertToDisplayUnit(valueInSquares);
        return String.format("%.0f%s", displayValue, getUnitAbbreviation());
    }

    private float convertToDisplayUnit(int valueInSquares) {
        return valueInSquares * getUnitsPerSquare();
    }

    protected float getUnitsPerSquare() {
        String language = Locale.getDefault().getLanguage();
        switch (language) {
            case "pt":
                return 1.5f;
            default:
                return 5;
        }
    }

    protected String getUnitAbbreviation() {
        return context.getString(R.string.distance_unit_abbrev);
    }

}
