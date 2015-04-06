package com.vituel.dndplayer.model;

import android.content.Context;

import com.vituel.dndplayer.R;

/**
 * Created by Victor on 26/02/14.
 */
public enum ModifierSource {

    STR, DEX, CON, INT, WIS, CHA;

    public String getLabel(Context ctx) {
        switch (this) {
            case STR:
                return ctx.getString(R.string.str);
            case DEX:
                return ctx.getString(R.string.dex);
            case CON:
                return ctx.getString(R.string.con);
            case INT:
                return ctx.getString(R.string.int_ability);
            case WIS:
                return ctx.getString(R.string.wis);
            case CHA:
                return ctx.getString(R.string.cha);
            default:
                return null;
        }
    }

}
