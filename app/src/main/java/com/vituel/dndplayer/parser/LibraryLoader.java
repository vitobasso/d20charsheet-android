package com.vituel.dndplayer.parser;

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
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.effect.Condition;
import com.vituel.dndplayer.parser.csv.AbstractEntityParser;
import com.vituel.dndplayer.parser.csv.BookParser;
import com.vituel.dndplayer.parser.csv.ClassParser;
import com.vituel.dndplayer.parser.csv.ClassTraitParser;
import com.vituel.dndplayer.parser.csv.ConditionParser;
import com.vituel.dndplayer.parser.csv.EditionParser;
import com.vituel.dndplayer.parser.csv.FeatParser;
import com.vituel.dndplayer.parser.csv.ItemParser;
import com.vituel.dndplayer.parser.csv.RaceParser;
import com.vituel.dndplayer.parser.csv.RaceTraitParser;
import com.vituel.dndplayer.parser.csv.SkillParser;
import com.vituel.dndplayer.parser.csv.TempEffectParser;
import com.vituel.dndplayer.parser.exception.ParseEntityException;
import com.vituel.dndplayer.util.gui.LoaderObserver;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Victor on 26/03/14.
 */
public class LibraryLoader {

    public static final String TAG = LibraryLoader.class.getSimpleName();

    private Context ctx;
    private LoaderObserver observer;

    public LibraryLoader(Context ctx, LoaderObserver observer) {
        this.ctx = ctx;
        this.observer = observer;
    }

    public void loadDB() {
        loadTable(new EditionParser(ctx, "data/csv/editions.csv"), R.string.editions, new EditionDao(ctx));
        loadTable(new BookParser(ctx, "data/csv/books.csv"), R.string.rulebooks, new BookDao(ctx));

        Cache cache = new Cache();

        ConditionParser conditionParser = new ConditionParser(ctx, "data/csv/conditions.csv");
        loadTable(conditionParser, R.string.conditionals, new ConditionDao(ctx));
        cache.cachedConditions = conditionParser.getTranslationMap();

        SkillParser skillParser = new SkillParser(ctx, "data/csv/skills.csv");
        loadTable(skillParser, R.string.skills, new SkillDao(ctx));
        cache.skillNameMap = skillParser.getTranslationMap();

        loadTable(new RaceParser(ctx, "data/csv/races.csv", cache), R.string.races, new RaceDao(ctx));
        loadTable(new RaceTraitParser(ctx, "data/csv/race_traits.csv", cache), R.string.race_traits, new RaceTraitDao(ctx));
        loadTable(new ClassParser(ctx, "data/csv/classes.csv"), R.string.classes, new ClassDao(ctx));
        loadTable(new ClassTraitParser(ctx, "data/csv/class_traits.csv", cache), R.string.class_traits, new ClassTraitDao(ctx));
        loadTable(new ItemParser(ctx, "data/csv/items.csv", cache), R.string.items, new ItemDao(ctx));
        loadTable(new FeatParser(ctx, "data/csv/feats.csv", cache), R.string.feats, new FeatDao(ctx));
        loadTable(new TempEffectParser(ctx, "data/csv/temp_effects.csv", cache), R.string.effects, new TempEffectDao(ctx));
    }

    private <T extends AbstractEntity> void loadTable(AbstractEntityParser<T> parser, int displayNameRes, AbstractDao<T> dao) {
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
            dao.insert(entity);
            observer.onFinishLoadingRow(entity.getName(), parser.getCount());
        } catch (ParseEntityException | SQLException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception when importing to " + dao.getClass().getSimpleName(), e);
        }
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
    }

    public static class Cache {
        public Map<Condition, Condition> cachedConditions;
        public Map<String, String> skillNameMap;
    }

}
