package com.vitobasso.d20charsheet.io.parser.csv;

import com.vitobasso.d20charsheet.io.importer.ImportContext;
import com.vitobasso.d20charsheet.io.parser.exception.ParseModifierException;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.effect.Modifier;

import java.util.Map;

import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SKILL;

/**
 * Created by Victor on 31/03/14.
 */
public class ModifierTranslatedParser extends ModifierParser {

    private Map<String, String> skillNameMap;
    private Map<Condition, Condition> conditionMap;

    public ModifierTranslatedParser(ImportContext importContext) {
        this.skillNameMap = importContext.getSkillNameMap();
        this.conditionMap = importContext.getCachedConditions();
    }

    public Modifier parse(String str) throws ParseModifierException {
        Modifier result = super.parse(str);
        translateSkillName(result);
        translateConditionName(result);
        return result;
    }

    private void translateSkillName(Modifier result) {
        if (result.getTarget() == SKILL) {
            String englishName = result.getVariation();
            String translatedName = skillNameMap.get(englishName);
            result.setVariation(translatedName);
        }
    }

    private void translateConditionName(Modifier result) {
        Condition englishCondition = result.getCondition();
        if (englishCondition != null) {
            Condition translatedCondition = conditionMap.get(englishCondition);
            result.setCondition(translatedCondition);
        }
    }

}
