package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.Attack;

/**
 * Created by Victor on 29/09/2014.
 */
public class WeaponRefStringConverter extends AbstractEnumStringConverter<Attack.WeaponReference> {

    public WeaponRefStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(Attack.WeaponReference type) {
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
