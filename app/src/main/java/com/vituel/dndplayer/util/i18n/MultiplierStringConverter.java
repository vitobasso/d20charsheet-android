package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.model.Multiplier;

/**
 * Created by Victor on 29/09/2014.
 */
public class MultiplierStringConverter extends AbstractEnumStringConverter<Multiplier> {

    public MultiplierStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(Multiplier type) {
        switch (type) {
            case HALF: return "×1/2";
            case ONE: return "×1";
            case ONE_AND_HALF: return "×1.5";
            case DOUBLE: return "×2";
            default: return type.toString();
        }
    }

}
