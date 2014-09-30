package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;

import static com.vituel.dndplayer.model.Attack.WeaponReferenceType;

/**
 * Created by Victor on 29/09/2014.
 */
public class WeaponRefStringConverter extends AbstractEnumStringConverter<WeaponReferenceType> {

    public WeaponRefStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(WeaponReferenceType type) {
        switch (type) {
            case MAIN_HAND:
                return findResource(R.string.main_hand);
            case OFFHAND:
                return findResource(R.string.offhand);
            default:
                return super.toString(type);
        }
    }

}
