package com.vituel.dndplayer.parser;

import android.content.Context;

import com.vituel.dndplayer.model.ModifierSource;
import com.vituel.dndplayer.model.Skill;

/**
 * Created by Victor on 26/03/14.
 */
public class SkillParser extends AbstractParser<Skill> {

    public SkillParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Skill parse(String line) {

        String split[] = line.split("\t");

        String name = read(split, 0);;
        String abilityStr = read(split, 1);
        String penaltyStr = read(split, 2);
        String synergyStr = read(split, 3);

        Skill result = new Skill(name);
        result.setKeyAbility(ModifierSource.valueOf(abilityStr));
        result.setArmorPenaltyApplies(penaltyStr != null && !penaltyStr.isEmpty());

        return result;
    }

}
