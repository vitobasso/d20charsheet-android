package com.vituel.dndplayer.io.parser;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.vituel.dndplayer.MemoryCache;
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
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Rule;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Victor on 26/03/14.
 */
public class RulesImporter {

    public static final String TAG = RulesImporter.class.getSimpleName();

    private Context ctx;
    private File dir;
    private ImporterObserver observer;
    private MemoryCache memoryCache;
    private Collection<Book> selectedBooks;

    public RulesImporter(Context ctx, ImporterObserver observer) {
        this.ctx = ctx;
        this.dir = RulesDownloader.getRulesDir(ctx);
        this.observer = observer;
        memoryCache = (MemoryCache) ctx.getApplicationContext();
    }

    public void loadDB() {
        loadBooks();
        selectedBooks = memoryCache.getActiveRulebooks();
        ParserCache parserCache = loadConditionsAndSkills();
        loadEntities(new RaceParser(ctx, getFile("races.csv"), parserCache), R.string.races, new RaceDao(ctx));
        loadEntities(new RaceTraitParser(ctx, getFile("race_traits.csv"), parserCache), R.string.race_traits, new RaceTraitDao(ctx));
        loadEntities(new ClassParser(ctx, getFile("classes.csv")), R.string.classes, new ClassDao(ctx));
        loadEntities(new ClassTraitParser(ctx, getFile("class_traits.csv"), parserCache), R.string.class_traits, new ClassTraitDao(ctx));
        loadEntities(new ItemParser(ctx, getFile("items.csv"), parserCache), R.string.items, new ItemDao(ctx));
        loadEntities(new FeatParser(ctx, getFile("feats.csv"), parserCache), R.string.feats, new FeatDao(ctx));
        loadEntities(new TempEffectParser(ctx, getFile("temp_effects.csv"), parserCache), R.string.effects, new TempEffectDao(ctx));
    }

    private void loadBooks() {
        loadEntities(new EditionParser(ctx, getFile("editions.csv")), R.string.editions, new EditionDao(ctx));
        loadEntities(new BookParser(ctx, getFile("books.csv")), R.string.rulebooks, new BookDao(ctx));
    }

    private ParserCache loadConditionsAndSkills() {
        ParserCache parserCache = new ParserCache();

        ConditionParser conditionParser = new ConditionParser(ctx, getFile("conditions.csv"));
        loadEntities(conditionParser, R.string.conditionals, new ConditionDao(ctx));
        parserCache.cachedConditions = conditionParser.getTranslationMap();

        SkillParser skillParser = new SkillParser(ctx, getFile("skills.csv"));
        loadEntities(skillParser, R.string.skills, new SkillDao(ctx));
        parserCache.skillNameMap = skillParser.getTranslationMap();
        return parserCache;
    }

    private <T extends AbstractEntity> void loadEntities(AbstractEntityParser<T> parser, int displayNameRes, AbstractDao<T> dao) {
        try {
            String displayName = ctx.getResources().getString(displayNameRes);
            observer.onStartLoadingFile(displayName);
            while (parser.hasNext()) {
                loadEntity(parser, dao);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException exception when importing to " + dao.getClass().getSimpleName(), e);
        } finally {
            close(parser, dao);
        }
    }

    private <T extends AbstractEntity> void loadEntity(AbstractEntityParser<T> parser, AbstractDao<T> dao) {
        try {
            T entity = parser.next();
            if (shouldImport(entity)) {
                dao.insert(entity);
                observer.onFinishLoadingRow(entity.getName(), parser.getCount());
            }
        } catch (ParseEntityException | SQLException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception when importing to " + dao.getClass().getSimpleName(), e);
        }
    }

    private <T extends AbstractEntity> boolean shouldImport(T entity) {
        if (entity instanceof Rule) {
            return isEntityInSelectedBook((Rule) entity);
        } else {
            return true;
        }
    }

    private <T extends Rule> boolean isEntityInSelectedBook(T entity) {
        final long bookId = entity.getBook().getId();
        Predicate<Book> sameId = new Predicate<Book>() {
            @Override
            public boolean apply(Book input) {
                return input != null && input.getId() == bookId;
            }
        };
        return Iterables.any(selectedBooks, sameId); //TODO sort csv and selectedBooks to reduce complexity
    }

    private <T extends AbstractEntity> void close(AbstractEntityParser<T> parser, AbstractDao<T> dao) {
        dao.close();
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
