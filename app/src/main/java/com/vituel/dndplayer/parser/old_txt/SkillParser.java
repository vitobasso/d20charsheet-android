package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;

import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 26/03/14.
 */
public class SkillParser extends AbstractSimpleParser<Skill> {

    public SkillParser(Context ctx, String filePath) {
        super(ctx, filePath);
    }

    @Override
    protected Skill parse(String[] line) throws ParseFieldException {

        String name = readString(line, 0);;
        String abilityStr = readString(line, 1);
        String penaltyStr = readString(line, 2);
        String synergyStr = readString(line, 3);

        Skill result = new Skill(name);
        result.setKeyAbility(ModifierSource.valueOf(abilityStr));
        result.setArmorPenaltyApplies(penaltyStr != null && !penaltyStr.isEmpty());

        return result;
    }

}
