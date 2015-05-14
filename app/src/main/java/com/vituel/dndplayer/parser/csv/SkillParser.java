package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 25/04/2015.
 */
public class SkillParser extends AbstractEntityParser<Skill> {

    public SkillParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Skill parse(String[] line, Skill result) throws ParseFieldException {
        result.setKeyAbility(readEnum(ModifierSource.class, line, "key ability"));
        result.setArmorPenaltyApplies(readBoolean(line, "armor penalty"));
        return result;
    }

    @Override
    protected Skill newInstance(String[] split) throws ParseFieldException {
        return new Skill();
    }

}
