package com.vituel.dndplayer.util.i18n;

import android.content.Context;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.effect.Condition;

import static com.vituel.dndplayer.model.effect.Condition.Predicate;

/**
 * Created by Victor on 29/09/2014.
 */
public class ConditionPredicateStringConverter extends AbstractEnumStringConverter<Condition.Predicate> {

    public ConditionPredicateStringConverter(Context ctx) {
        super(ctx);
    }

    public CharSequence toString(Predicate pred) {
        switch (pred) {
            case WHEN:
                return findResource(R.string.when);
            case USING:
                return findResource(R.string.using);
            case AGAINST:
                return findResource(R.string.against);
            case RELATED_TO:
                return findResource(R.string.related_to);
            default:
                return super.toString(pred);
        }
    }

}
