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

    private Context ctx;
    private File dir;
    private ImporterObserver observer;

    public RulesImporter(Context ctx, ImporterObserver observer) {
        this.ctx = ctx;
        this.dir = RulesDownloader.getRulesDir(ctx);
        this.observer = observer;
    }

    public void loadDB() {
        loadTable(new EditionParser(ctx, getFile("editions.csv")), R.string.editions, new EditionDao(ctx));
        loadTable(new BookParser(ctx, getFile("books.csv")), R.string.rulebooks, new BookDao(ctx));

        Cache cache = new Cache();

        ConditionParser conditionParser = new ConditionParser(ctx, getFile("conditions.csv"));
        loadTable(conditionParser, R.string.conditionals, new ConditionDao(ctx));
        cache.cachedConditions = conditionParser.getTranslationMap();

        SkillParser skillParser = new SkillParser(ctx, getFile("skills.csv"));
        loadTable(skillParser, R.string.skills, new SkillDao(ctx));
        cache.skillNameMap = skillParser.getTranslationMap();

        loadTable(new RaceParser(ctx, getFile("races.csv"), cache), R.string.races, new RaceDao(ctx));
        loadTable(new RaceTraitParser(ctx, getFile("race_traits.csv"), cache), R.string.race_traits, new RaceTraitDao(ctx));
        loadTable(new ClassParser(ctx, getFile("classes.csv")), R.string.classes, new ClassDao(ctx));
        loadTable(new ClassTraitParser(ctx, getFile("class_traits.csv"), cache), R.string.class_traits, new ClassTraitDao(ctx));
        loadTable(new ItemParser(ctx, getFile("items.csv"), cache), R.string.items, new ItemDao(ctx));
        loadTable(new FeatParser(ctx, getFile("feats.csv"), cache), R.string.feats, new FeatDao(ctx));
        loadTable(new TempEffectParser(ctx, getFile("temp_effects.csv"), cache), R.string.effects, new TempEffectDao(ctx));
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

    private File getFile(String fileName) {
        return new File(dir, fileName);
    }

    public static class Cache {
        public Map<Condition, Condition> cachedConditions;
        public Map<String, String> skillNameMap;
    }

}
