package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.effect.ModifierType;

/**
 * Created by Victor on 29/09/2014.
 */
public class ModifierTypeStringConverter extends AbstractEnumStringConverter<ModifierType> {

    public ModifierTypeStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(ModifierType type) {
        switch (type) {
            case ENHANCEMENT:
                return findResource(R.string.enhance);
            case MORALE:
                return findResource(R.string.morale);
            case COMPETENCE:
                return findResource(R.string.compete);
            case CIRCUMSTANCE:
                return findResource(R.string.circums);
            case ARMOR:
                return findResource(R.string.armor);
            case SHIELD:
                return findResource(R.string.shield);
            case NATURAL_ARMOR:
                return findResource(R.string.nat_armor);
            case DEFLECTION:
                return findResource(R.string.defl);
            case DODGE:
                return findResource(R.string.dodge);
            case SIZE:
                return findResource(R.string.size);
            case RACIAL:
                return findResource(R.string.racial);
            case LUCK:
                return findResource(R.string.luck);
            case ALCHEMICAL:
                return findResource(R.string.alchem);
            case INSIGHT:
                return findResource(R.string.insight);
            case RESISTANCE:
                return findResource(R.string.resist);
            case INHERENT:
                return findResource(R.string.inherent);
            case PROFANE:
                return findResource(R.string.profane);
            case SACRED:
                return findResource(R.string.sacred);
            default:
                return super.toString(type);
        }
    }

}
