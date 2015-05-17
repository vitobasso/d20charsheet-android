package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;

/**
 * Created by Victor on 29/09/2014.
 */
public class ConditionTranslator {

    private Context ctx;

    public ConditionTranslator(Context ctx) {
        this.ctx = ctx;
    }

    public String translate(String conditionName) {
        return findResourceForName(conditionName).toString();
    }

    private CharSequence findResourceForName(String conditionName) {
        switch (conditionName) {
            case "melee":
                return findResource(R.string.melee);
            case "ranged":
                return findResource(R.string.ranged);
            case "not moving":
                return findResource(R.string.not_moving);
            case "poison":
                return findResource(R.string.poison);
            case "disease":
                return findResource(R.string.disease);
            case "fear":
                return findResource(R.string.fear);
            case "enchantment":
                return findResource(R.string.enchantment);
            case "aging":
                return findResource(R.string.aging);
            case "burst":
                return findResource(R.string.burst);
            case "aberration":
                return findResource(R.string.aberration);
            case "animal":
                return findResource(R.string.animal);
            case "construct":
                return findResource(R.string.construct);
            case "dragon":
                return findResource(R.string.dragon);
            case "elemental":
                return findResource(R.string.elemental);
            case "fey":
                return findResource(R.string.fey);
            case "giant":
                return findResource(R.string.giants);
            case "humanoid":
                return findResource(R.string.humanoid);
            case "magical beast":
                return findResource(R.string.magical_beast);
            case "monstrous humanoid":
                return findResource(R.string.monstrous_humanoid);
            case "ooze":
                return findResource(R.string.ooze);
            case "outsider":
                return findResource(R.string.outsider);
            case "plant":
                return findResource(R.string.plant);
            case "undead":
                return findResource(R.string.undead);
            case "vermin":
                return findResource(R.string.vermin);
            default:
                return conditionName;
        }
    }

    private CharSequence findResource(int resourceId) {
        return ctx.getResources().getString(resourceId);
    }

}
