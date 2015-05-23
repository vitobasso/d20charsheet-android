package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.effect.ModifierSource;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 25/04/2015.
 */
public class SkillParser extends AbstractEntityParser<Skill> {

    private Map<String, String> translationMap = new HashMap<>();

    public SkillParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Skill parse(String[] line, Skill result) throws ParseFieldException {
        result.setKeyAbility(readEnum(ModifierSource.class, line, "key ability"));
        result.setArmorPenaltyApplies(readBoolean(line, "armor penalty"));

        addToTranslatedNameMap(line, result);

        return result;
    }

    @Override
    protected Skill newInstance(String[] split) throws ParseFieldException {
        return new Skill();
    }

    private void addToTranslatedNameMap(String[] line, Skill result) throws ParseFieldException {
        String englishName = readString(line, HEADER_NAME_DEFAULT);
        translationMap.put(englishName, result.getName());
    }

    public Map<String, String> getTranslationMap() {
        return translationMap;
    }

}
