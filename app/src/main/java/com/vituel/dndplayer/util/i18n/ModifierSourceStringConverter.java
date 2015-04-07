package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.ModifierSource;

/**
 * Created by Victor on 29/09/2014.
 */
public class ModifierSourceStringConverter extends AbstractEnumStringConverter<ModifierSource> {

    public ModifierSourceStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(ModifierSource type) {
        switch (type) {
            case STR:
                return findResource(R.string.str);
            case DEX:
                return findResource(R.string.dex);
            case CON:
                return findResource(R.string.con);
            case INT:
                return findResource(R.string.int_ability);
            case WIS:
                return findResource(R.string.wis);
            case CHA:
                return findResource(R.string.cha);
            default:
                return super.toString(type);
        }
    }

}
