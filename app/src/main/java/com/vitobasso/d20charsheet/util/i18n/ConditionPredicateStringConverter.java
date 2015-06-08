package com.vitobasso.d20charsheet.util.i18n;

import android.content.Context;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.effect.Condition;

import static com.vitobasso.d20charsheet.model.effect.Condition.Predicate;

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
