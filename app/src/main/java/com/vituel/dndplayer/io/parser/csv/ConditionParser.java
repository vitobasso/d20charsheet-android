package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.effect.Condition;

import java.util.HashMap;
import java.util.Map;

import static com.vituel.dndplayer.model.effect.Condition.Predicate;

/**
 * Created by Victor on 31/03/14.
 */
public class ConditionParser extends AbstractEntityParser<Condition> {

    private Map<Condition, Condition> translationMap = new HashMap<>();

    public ConditionParser(Context ctx, String path) {
        super(ctx, path);
    }

    @Override
    protected Condition parse(String[] line, Condition result) throws ParseFieldException {
        Predicate predicate = Predicate.fromString(readString(line, "predicate"));
        result.setPredicate(predicate);
        result.setParent(readParent(line, "parent"));

        addedToTranslationMap(line, result);
        return result;
    }

    private Condition readParent(String[] line, String column) throws ParseFieldException {
        Integer parentId = readIntNullable(line, column);
        if (parentId != null) {
            Condition condition = new Condition();
            condition.setId(parentId);
            return condition;
        } else {
            return null;
        }
    }

    private void addedToTranslationMap(String[] line, Condition result) throws ParseFieldException {
        String englishName = readString(line, HEADER_NAME_DEFAULT);
        Condition englishCondition = new Condition();
        englishCondition.setName(englishName);
        englishCondition.setPredicate(result.getPredicate());
        translationMap.put(englishCondition, result);
    }

    public Map<Condition, Condition> getTranslationMap() {
        return translationMap;
    }

    @Override
    protected Condition newInstance(String[] split) throws ParseFieldException {
        return new Condition();
    }

}
