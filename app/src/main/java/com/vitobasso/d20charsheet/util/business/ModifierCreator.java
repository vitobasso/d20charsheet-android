package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSkill;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.AbilityModifier;
import com.vitobasso.d20charsheet.model.effect.Modifier;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.AC;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CHA;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CON;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DEX;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.HIT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.HP;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.INT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SKILL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.STR;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.WIS;

/**
 * Created by Victor on 27/06/2015.
 */
public class ModifierCreator {

    private Context context;
    private CharSummary summary;
    private CharBase base;

    public ModifierCreator(Context context, CharSummary summary) {
        this.context = context;
        this.summary = summary;
        this.base = summary.getBase();
    }

    public List<Modifier> createBaseModifiers() {
        List<Modifier> modifiers = new ArrayList<>();

        //hp and ac
        modifiers.add(new Modifier(HP, base.getHitPoints()));
        modifiers.add(new Modifier(AC, 10));

        //abilities
        modifiers.add(new Modifier(STR, base.getStrength()));
        modifiers.add(new Modifier(DEX, base.getDexterity()));
        modifiers.add(new Modifier(CON, base.getConstitution()));
        modifiers.add(new Modifier(INT, base.getIntelligence()));
        modifiers.add(new Modifier(WIS, base.getWisdom()));
        modifiers.add(new Modifier(CHA, base.getCharisma()));

        //skills
        for (CharSkill skillGrad : base.getSkills()) {
            modifiers.add(new Modifier(SKILL, skillGrad.getSkill().getName(), skillGrad.getScore()));
        }

        return modifiers;
    }

    public Map<Modifier, String> createAbilityModifiers() {
        Map<Modifier, String> result = new HashMap<>();

        //hp = con * level
        int lvl = base.getExperienceLevel();
        int conMod = summary.getAbilityModifier(ModifierSource.CON);
        String conLvlLabel = String.format("%s × %s", getString(R.string.con), getString(R.string.level));
        result.put(new Modifier(HP, lvl * conMod), conLvlLabel);

        //others
        for (AbilityModifier mod : base.getAbilityMods()) {
            int value = summary.getAbilityModifier(mod.getAbility());
            value = mod.getMultiplier().multiply(value);
            Modifier resultingMod = new Modifier(mod.getTarget(), mod.getVariation(), value);
            String label = mod.getAbility().getLabel(context);
            result.put(resultingMod, label);
        }

        return result;
    }

    public List<Modifier> createSizeModifiers() {
        List<Modifier> modifiers = new ArrayList<>();
        modifiers.add(new Modifier(HIT, summary.getSize().getCombatModifier()));
        modifiers.add(new Modifier(AC, summary.getSize().getCombatModifier()));
        return modifiers;
    }

    private String getString(int res) {
        return context.getResources().getString(res);
    }

}
