package com.vitobasso.d20charsheet.util.factory;

import android.content.Context;

import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.ClassTrait;
import com.vitobasso.d20charsheet.model.Clazz;
import com.vitobasso.d20charsheet.model.Feat;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.RaceTrait;
import com.vitobasso.d20charsheet.model.Skill;
import com.vitobasso.d20charsheet.model.TempEffect;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.item.Item;
import com.vitobasso.d20charsheet.model.rulebook.Book;
import com.vitobasso.d20charsheet.model.rulebook.Edition;
import com.vitobasso.d20charsheet.util.factory.impl.BookFactory;
import com.vitobasso.d20charsheet.util.factory.impl.ClassFactory;
import com.vitobasso.d20charsheet.util.factory.impl.ClassTraitFactory;
import com.vitobasso.d20charsheet.util.factory.impl.ConditionFactory;
import com.vitobasso.d20charsheet.util.factory.impl.EditionFactory;
import com.vitobasso.d20charsheet.util.factory.impl.FeatFactory;
import com.vitobasso.d20charsheet.util.factory.impl.ItemFactory;
import com.vitobasso.d20charsheet.util.factory.impl.RaceFactory;
import com.vitobasso.d20charsheet.util.factory.impl.RaceTraitFactory;
import com.vitobasso.d20charsheet.util.factory.impl.SkillFactory;
import com.vitobasso.d20charsheet.util.factory.impl.TempEffectFactory;

/**
 * Created by Victor on 15/06/2015.
 */
public class EntityToolFactories {

    @SuppressWarnings("unchecked")
    public static <T extends AbstractEntity> EntityToolFactory<T> createFactory(Class<T> entityClass, Context ctx) {
        return (EntityToolFactory<T>) instantiateFactory(entityClass, ctx);
    }

    private static <T extends AbstractEntity> EntityToolFactory instantiateFactory(Class<T> entityClass, Context ctx) {
        if (entityClass == Edition.class) {
            return new EditionFactory(ctx);
        } else if (entityClass == Book.class) {
            return new BookFactory(ctx);
        } else if (entityClass == Condition.class) {
            return new ConditionFactory(ctx);
        } else if (entityClass == Skill.class) {
            return new SkillFactory(ctx);
        } else if (entityClass == Race.class) {
            return new RaceFactory(ctx);
        } else if (entityClass == RaceTrait.class) {
            return new RaceTraitFactory(ctx);
        } else if (entityClass == Clazz.class) {
            return new ClassFactory(ctx);
        } else if (entityClass == ClassTrait.class) {
            return new ClassTraitFactory(ctx);
        } else if (entityClass == Item.class) {
            return new ItemFactory(ctx);
        } else if (entityClass == Feat.class) {
            return new FeatFactory(ctx);
        } else if (entityClass == TempEffect.class) {
            return new TempEffectFactory(ctx);
        } else {
            throw new IllegalArgumentException(entityClass.toString());
        }
    }

}
