package com.vituel.dndplayer.io.parser.csv;

import com.vituel.dndplayer.io.parser.RulesImporter;
import com.vituel.dndplayer.io.parser.exception.ParseModifierException;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.model.effect.Modifier;

import java.util.Map;

import static com.vituel.dndplayer.model.effect.ModifierTarget.SKILL;

/**
 * Created by Victor on 31/03/14.
 */
public class ModifierTranslatedParser extends ModifierParser {

    private Map<String, String> skillNameMap;
    private Map<Condition, Condition> conditionMap;

    public ModifierTranslatedParser(RulesImporter.ParserCache loadingCache) {
        this.skillNameMap = loadingCache.skillNameMap;
        this.conditionMap = loadingCache.cachedConditions;
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
