package com.vitobasso.d20charsheet.io.importer;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractDao;
import com.vitobasso.d20charsheet.dao.dependant.ClassTraitDao;
import com.vitobasso.d20charsheet.dao.dependant.RaceTraitDao;
import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.dao.entity.ClassDao;
import com.vitobasso.d20charsheet.dao.entity.ConditionDao;
import com.vitobasso.d20charsheet.dao.entity.EditionDao;
import com.vitobasso.d20charsheet.dao.entity.FeatDao;
import com.vitobasso.d20charsheet.dao.entity.ItemDao;
import com.vitobasso.d20charsheet.dao.entity.RaceDao;
import com.vitobasso.d20charsheet.dao.entity.SkillDao;
import com.vitobasso.d20charsheet.dao.entity.TempEffectDao;
import com.vitobasso.d20charsheet.io.parser.csv.AbstractEntityParser;
import com.vitobasso.d20charsheet.io.parser.csv.BookParser;
import com.vitobasso.d20charsheet.io.parser.csv.ClassParser;
import com.vitobasso.d20charsheet.io.parser.csv.ClassTraitParser;
import com.vitobasso.d20charsheet.io.parser.csv.ConditionParser;
import com.vitobasso.d20charsheet.io.parser.csv.EditionParser;
import com.vitobasso.d20charsheet.io.parser.csv.FeatParser;
import com.vitobasso.d20charsheet.io.parser.csv.ItemParser;
import com.vitobasso.d20charsheet.io.parser.csv.RaceParser;
import com.vitobasso.d20charsheet.io.parser.csv.RaceTraitParser;
import com.vitobasso.d20charsheet.io.parser.csv.SkillParser;
import com.vitobasso.d20charsheet.io.parser.csv.TempEffectParser;
import com.vitobasso.d20charsheet.io.parser.exception.ParseEntityException;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.util.LoggingUtil;
import com.vitobasso.d20charsheet.util.database.BulkInserter;

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

    public void importCsvs() {
        long startTime = System.currentTimeMillis(); //TODO use AOP instead?
        doImportCsvs();
        LoggingUtil.logTime(TAG, startTime, "import rules");
    }

    private void doImportCsvs() {
        importBooks();
        ParserCache parserCache = importConditionsAndSkills();
        importCsv(new RaceParser(ctx, getFile("races.csv"), parserCache), R.string.races, new RaceDao(ctx));
        importCsv(new RaceTraitParser(ctx, getFile("race_traits.csv"), parserCache), R.string.race_traits, new RaceTraitDao(ctx));
        importCsv(new ClassParser(ctx, getFile("classes.csv")), R.string.classes, new ClassDao(ctx));
        importCsv(new ClassTraitParser(ctx, getFile("class_traits.csv"), parserCache), R.string.class_traits, new ClassTraitDao(ctx));
        importCsv(new ItemParser(ctx, getFile("items.csv"), parserCache), R.string.items, new ItemDao(ctx));
        importCsv(new FeatParser(ctx, getFile("feats.csv"), parserCache), R.string.feats, new FeatDao(ctx));
        importCsv(new TempEffectParser(ctx, getFile("temp_effects.csv"), parserCache), R.string.effects, new TempEffectDao(ctx));
    }

    private void importBooks() {
        importCsv(new EditionParser(ctx, getFile("editions.csv")), R.string.editions, new EditionDao(ctx));
        importCsv(new BookParser(ctx, getFile("books.csv")), R.string.rulebooks, new BookDao(ctx));
    }

    private ParserCache importConditionsAndSkills() {
        ParserCache parserCache = new ParserCache();

        ConditionParser conditionParser = new ConditionParser(ctx, getFile("conditions.csv"));
        importCsv(conditionParser, R.string.conditionals, new ConditionDao(ctx));
        parserCache.cachedConditions = conditionParser.getTranslationMap();

        SkillParser skillParser = new SkillParser(ctx, getFile("skills.csv"));
        importCsv(skillParser, R.string.skills, new SkillDao(ctx));
        parserCache.skillNameMap = skillParser.getTranslationMap();
        return parserCache;
    }

    private <T extends AbstractEntity> void importCsv(AbstractEntityParser<T> parser, int displayNameRes, AbstractDao<T> dao) {
        try {
            tryImportCsv(parser, displayNameRes, dao);
        } catch (IOException e) {
            Log.e(TAG, "IOException exception when importing to " + dao.getClass().getSimpleName(), e);
        } finally {
            close(parser, dao);
        }
    }

    private <T extends AbstractEntity> void tryImportCsv(AbstractEntityParser<T> parser, int displayNameRes, AbstractDao<T> dao) throws IOException {
        String displayName = ctx.getResources().getString(displayNameRes);
        observer.onBeginFile(displayName);
        BulkInserter<T> bulkInserter = dao.createBulkInserter();
        while (parser.hasNext()) {
            importBatch(parser, bulkInserter);
        }
    }

    private <T extends AbstractEntity> void importBatch(AbstractEntityParser<T> parser, BulkInserter<T> bulkInserter) throws IOException {
        try {
            bulkInserter.beginTransaction();
            tryImportBatch(parser, bulkInserter);
            bulkInserter.markAsSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Batch import was interrupted.", e);
            throw e;
        } finally {
            bulkInserter.endTransaction();
        }
    }

    private <T extends AbstractEntity> void tryImportBatch(AbstractEntityParser<T> parser, BulkInserter<T> bulkInserter) throws IOException {
        for (int i = 0; i < BATCH_SIZE && parser.hasNext(); i++) {
            importEntity(parser, bulkInserter);
        }
        observer.onFinishBatch(parser.getCount());
    }

    private <T extends AbstractEntity> void importEntity(AbstractEntityParser<T> parser, BulkInserter<T> bulkInserter) {
        try {
            tryImportEntity(parser, bulkInserter);
        } catch (ParseEntityException | SQLException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception when importing to " + bulkInserter.getClass().getSimpleName(), e);
        }
    }

    private <T extends AbstractEntity> void tryImportEntity(AbstractEntityParser<T> parser, BulkInserter<T> bulkInserter) throws IOException {
        T entity = parser.next();
        bulkInserter.insert(entity);
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
    }

    private File getFile(String fileName) {
        return new File(dir, fileName);
    }

    public static class ParserCache {
        public Map<Condition, Condition> cachedConditions;
        public Map<String, String> skillNameMap;
    }

}
