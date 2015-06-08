package com.vitobasso.d20charsheet.util.i18n;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.item.SlotType;

/**
 * Created by Victor on 29/09/2014.
 */
public class SlotTypeStringConverter extends AbstractEnumStringConverter<SlotType> {

    public SlotTypeStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(SlotType type) {
        switch (type) {
            case ARMS:
                return findResource(R.string.arms);
            case BODY:
                return findResource(R.string.body);
            case EYES:
                return findResource(R.string.eyes);
            case FEET:
                return findResource(R.string.feet);
            case FINGER:
                return findResource(R.string.finger);
            case HANDS:
                return findResource(R.string.hands);
            case HELD:
                return findResource(R.string.held);
            case HEAD:
                return findResource(R.string.head);
            case NECK:
                return findResource(R.string.neck);
            case TORSO:
                return findResource(R.string.torso);
            case WAIST:
                return findResource(R.string.waist);
            case SHOULDERS:
                return findResource(R.string.shoulders);
            default:
                return super.toString(type);
        }
    }

}
