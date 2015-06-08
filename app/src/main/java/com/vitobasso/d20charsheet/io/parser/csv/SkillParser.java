package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Victor on 25/04/2015.
 */
public class SkillParser extends AbstractEntityParser<Skill> {

    private Map<String, String> translationMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public SkillParser(Context ctx, File file) {
        super(ctx, file);
    }

    @Override
    protected Skill parse(String[] line, Skill result) throws ParseFieldException {
        result.setBook(readRulebook(line));
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
