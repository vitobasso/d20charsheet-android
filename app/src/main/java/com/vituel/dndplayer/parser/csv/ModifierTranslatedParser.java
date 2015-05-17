package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.model.effect.Modifier;
import com.vituel.dndplayer.parser.exception.ParseModifierException;
import com.vituel.dndplayer.util.i18n.ConditionTranslator;

import java.util.Map;

import static com.vituel.dndplayer.model.effect.ModifierTarget.SKILL;

/**
 * Created by Victor on 31/03/14.
 */
public class ModifierTranslatedParser extends ModifierParser {

    private Map<String, String> skillNameMap;
    private ConditionTranslator conditionTranslator;

    public ModifierTranslatedParser(Context context, Map<String, String> skillNameMap) {
        this.skillNameMap = skillNameMap;
        this.conditionTranslator = new ConditionTranslator(context);
    }

    public Modifier parse(String str) throws ParseModifierException {
        Modifier result = super.parse(str);
        translateSkillName(result);
        translateConditionName(result);
        return result;
    }

    private void translateConditionName(Modifier result) {
        Condition condition = result.getCondition();
        if (condition != null) {
            String englishName = condition.getName();
            String translatedName = conditionTranslator.translate(englishName);
            condition.setName(translatedName);
        }
    }

    private void translateSkillName(Modifier result) {
        if (result.getTarget() == SKILL) {
            String englishName = result.getVariation();
            String translatedName = skillNameMap.get(englishName);
            result.setVariation(translatedName);
        }
    }

}
