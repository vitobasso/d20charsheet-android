package com.vitobasso.d20charsheet.io.importer;

import android.content.Context;

import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.ConditionParser;
import com.vitobasso.d20charsheet.io.parser.csv.SkillParser;
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
import com.vitobasso.d20charsheet.util.LoggingUtil;
import com.vitobasso.d20charsheet.util.factory.EffectEntityToolFactory;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactories;
import com.vitobasso.d20charsheet.util.factory.EntityToolFactory;

/**
 * Created by Victor on 26/03/14.
 */
public class RulesImporter {

    public static final String TAG = RulesImporter.class.getSimpleName();

    private Context ctx;
    private ImporterObserver observer;
    private ImportContext importContext;

    public RulesImporter(Context ctx, ImporterObserver observer) {
        this.ctx = ctx;
        this.observer = observer;
        this.importContext = new ImportContext();
    }

    public void importCsvs() {
        long startTime = System.currentTimeMillis(); //TODO use AOP instead?
        importAllEntities();
        LoggingUtil.logTime(TAG, startTime, "import rules");
    }

    private void importAllEntities() {
        importRules(Edition.class);
        importRules(Book.class);
        importRules(Condition.class); //needs to be imported before entities with effect (results are cached for modifier parsing)
        importRules(Skill.class); //needs to be imported before entities with effect (results are cached for modifier parsing)
        importRules(Race.class);
        importRules(RaceTrait.class);
        importRules(Clazz.class);
        importRules(ClassTrait.class);
        importRules(Item.class);
        importRules(Feat.class);
        importRules(TempEffect.class);
    }

    private <T extends AbstractEntity> void importRules(Class<T> entityClass) {
        EntityToolFactory<T> factory = createFactory(entityClass, ctx, importContext);
        RulesEntityImporter<T> importer = new RulesEntityImporter<>(factory, observer);
        importer.importRules();
        cacheResults(importer);
    }

    public int getTotalFiles() {
        return 11;
    }

    private <T extends AbstractEntity> EntityToolFactory<T> createFactory(Class<T> entityClass, Context ctx, ImportContext cache) {
        EntityToolFactory<T> factory = EntityToolFactories.createFactory(entityClass, ctx);
        if (factory instanceof EffectEntityToolFactory) {
            EffectEntityToolFactory<T> effectFactory = (EffectEntityToolFactory<T>) factory;
            effectFactory.setImportContext(cache);
        }
        return factory;
    }

    private <T extends AbstractEntity>  void cacheResults(RulesEntityImporter<T> importer) {
        AbstractEntityParser<T> parser = importer.getParser();
        if (parser instanceof SkillParser) {
            SkillParser skillParser = (SkillParser) parser;
            importContext.setSkillNameMap(skillParser.getTranslationMap());
        } else if (parser instanceof ConditionParser) {
            ConditionParser conditionParser = (ConditionParser) parser;
            importContext.setCachedConditions(conditionParser.getTranslationMap());
        }
    }

}
