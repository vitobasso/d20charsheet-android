package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

/**
 * Created by Victor on 25/04/2015.
 */
public class SkillParser extends AbstractCsvParser<Skill> {

    public SkillParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Skill parse(String[] line) throws ParseFieldException {
        Skill result = new Skill();
        result.setId(readInt(line, "id"));
        result.setName(readString(line, "name"));
        result.setKeyAbility(readEnum(ModifierSource.class, line, "key ability"));
        result.setArmorPenaltyApplies(readBoolean(line, "armor penalty"));
        return result;
    }

}
