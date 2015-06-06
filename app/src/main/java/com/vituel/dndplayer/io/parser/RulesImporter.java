package com.vituel.dndplayer.io.parser;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.dao.abstraction.AbstractDao;
import com.vituel.dndplayer.dao.dependant.ClassTraitDao;
import com.vituel.dndplayer.dao.dependant.RaceTraitDao;
import com.vituel.dndplayer.dao.entity.BookDao;
import com.vituel.dndplayer.dao.entity.ClassDao;
import com.vituel.dndplayer.dao.entity.ConditionDao;
import com.vituel.dndplayer.dao.entity.EditionDao;
import com.vituel.dndplayer.dao.entity.FeatDao;
import com.vituel.dndplayer.dao.entity.ItemDao;
import com.vituel.dndplayer.dao.entity.RaceDao;
import com.vituel.dndplayer.dao.entity.SkillDao;
import com.vituel.dndplayer.dao.entity.TempEffectDao;
import com.vituel.dndplayer.io.downloader.RulesDownloader;
import com.vituel.dndplayer.io.parser.csv.AbstractEntityParser;
import com.vituel.dndplayer.io.parser.csv.BookParser;
import com.vituel.dndplayer.io.parser.csv.ClassParser;
import com.vituel.dndplayer.io.parser.csv.ClassTraitParser;
import com.vituel.dndplayer.io.parser.csv.ConditionParser;
import com.vituel.dndplayer.io.parser.csv.EditionParser;
import com.vituel.dndplayer.io.parser.csv.FeatParser;
import com.vituel.dndplayer.io.parser.csv.ItemParser;
import com.vituel.dndplayer.io.parser.csv.RaceParser;
import com.vituel.dndplayer.io.parser.csv.RaceTraitParser;
import com.vituel.dndplayer.io.parser.csv.SkillParser;
import com.vituel.dndplayer.io.parser.csv.TempEffectParser;
import com.vituel.dndplayer.io.parser.exception.ParseEntityException;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.effect.Condition;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Victor on 26/03/14.
 */
public class RulesImporter {

    public static final String TAG = RulesImporter.class.getSimpleName();
    public static final int BATCH_SIZE = 100;

    private Context ctx;
    private File dir;
    private ImporterObserver observer;

    public RulesImporter(Context ctx, ImporterObserver observer) {
        this.ctx = ctx;
        this.dir = RulesDownloader.getRulesDir(ctx);
        this.observer = observer;
    }

    public void loadDB() {
        importBooks();
        ParserCache parserCache = importConditionsAndSkills();
        importAll(new RaceParser(ctx, getFile("races.csv"), parserCache), R.string.races, new RaceDao(ctx));
        importAll(new RaceTraitParser(ctx, getFile("race_traits.csv"), parserCache), R.string.race_traits, new RaceTraitDao(ctx));
        importAll(new ClassParser(ctx, getFile("classes.csv")), R.string.classes, new ClassDao(ctx));
        importAll(new ClassTraitParser(ctx, getFile("class_traits.csv"), parserCache), R.string.class_traits, new ClassTraitDao(ctx));
        importAll(new ItemParser(ctx, getFile("items.csv"), parserCache), R.string.items, new ItemDao(ctx));
        importAll(new FeatParser(ctx, getFile("feats.csv"), parserCache), R.string.feats, new FeatDao(ctx));
        importAll(new TempEffectParser(ctx, getFile("temp_effects.csv"), parserCache), R.string.effects, new TempEffectDao(ctx));
    }

    private void importBooks() {
        importAll(new EditionParser(ctx, getFile("editions.csv")), R.string.editions, new EditionDao(ctx));
        importAll(new BookParser(ctx, getFile("books.csv")), R.string.rulebooks, new BookDao(ctx));
    }

    private ParserCache importConditionsAndSkills() {
        ParserCache parserCache = new ParserCache();

        ConditionParser conditionParser = new ConditionParser(ctx, getFile("conditions.csv"));
        importAll(conditionParser, R.string.conditionals, new ConditionDao(ctx));
        parserCache.cachedConditions = conditionParser.getTranslationMap();

        SkillParser skillParser = new SkillParser(ctx, getFile("skills.csv"));
        importAll(skillParser, R.string.skills, new SkillDao(ctx));
        parserCache.skillNameMap = skillParser.getTranslationMap();
        return parserCache;
    }

    private <T extends AbstractEntity> void importAll(AbstractEntityParser<T> parser, int displayNameRes, AbstractDao<T> dao) {
        try {
            tryImportAll(parser, displayNameRes, dao);
        } catch (IOException e) {
            Log.e(TAG, "IOException exception when importing to " + dao.getClass().getSimpleName(), e);
        } finally {
            close(parser, dao);
        }
    }

    private <T extends AbstractEntity> void tryImportAll(AbstractEntityParser<T> parser, int displayNameRes, AbstractDao<T> dao) throws IOException {
        String displayName = ctx.getResources().getString(displayNameRes);
        observer.onStartImportingFile(displayName);
        while (parser.hasNext()) {
            importBatch(parser, dao);
        }
    }

    private <T extends AbstractEntity> void importBatch(AbstractEntityParser<T> parser, AbstractDao<T> dao) throws IOException {
        try {
            dao.beginTransaction();
            tryImportBatch(parser, dao);
            dao.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Batch import was interrupted.", e);
            throw e;
        } finally {
            dao.endTransaction();
        }
    }

    private <T extends AbstractEntity> void tryImportBatch(AbstractEntityParser<T> parser, AbstractDao<T> dao) throws IOException {
        for (int i = 0; i < BATCH_SIZE && parser.hasNext(); i++) {
            importEntity(parser, dao);
        }
    }

    private <T extends AbstractEntity> void importEntity(AbstractEntityParser<T> parser, AbstractDao<T> dao) {
        try {
            tryImportEntity(parser, dao);
        } catch (ParseEntityException | SQLException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception when importing to " + dao.getClass().getSimpleName(), e);
        }
    }

    private <T extends AbstractEntity> void tryImportEntity(AbstractEntityParser<T> parser, AbstractDao<T> dao) throws IOException {
        T entity = parser.next();
        dao.insert(entity);
        observer.onFinishImportingRow(entity.getName(), parser.getCount());
    }

    private <T extends AbstractEntity> void close(AbstractEntityParser<T> parser, AbstractDao<T> dao) {
        closeDao(dao);
        closeParser(parser);
    }

    private <T extends AbstractEntity> void closeDao(AbstractDao<T> dao) {
        try {
            dao.close();
        } catch (RuntimeException e) {
            Log.e(TAG, "Failed to close dao.", e);
        }
    }

    private <T extends AbstractEntity> void closeParser(AbstractEntityParser<T> parser) {
        try {
            parser.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close parser.", e);
        }
    }

    public int getTotalFiles() {
        return 11;
    } //TODO get w/ the csvs somehow? even row counts?

    private File getFile(String fileName) {
        return new File(dir, fileName);
    }

    public static class ParserCache {
        public Map<Condition, Condition> cachedConditions;
        public Map<String, String> skillNameMap;
    }

}
