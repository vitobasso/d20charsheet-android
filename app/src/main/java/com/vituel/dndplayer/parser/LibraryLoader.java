package com.vituel.dndplayer.parser;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.vituel.dndplayer.dao.abstraction.AbstractDao;
import com.vituel.dndplayer.dao.dependant.ClassTraitDao;
import com.vituel.dndplayer.dao.entity.BookDao;
import com.vituel.dndplayer.dao.entity.ClassDao;
import com.vituel.dndplayer.dao.entity.EditionDao;
import com.vituel.dndplayer.dao.entity.FeatDao;
import com.vituel.dndplayer.dao.entity.ItemDao;
import com.vituel.dndplayer.dao.entity.RaceDao;
import com.vituel.dndplayer.dao.entity.SkillDao;
import com.vituel.dndplayer.dao.entity.TempEffectDao;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.parser.csv.AbstractCsvParser;
import com.vituel.dndplayer.parser.csv.BookParser;
import com.vituel.dndplayer.parser.csv.ClassParser;
import com.vituel.dndplayer.parser.csv.ClassTraitParser;
import com.vituel.dndplayer.parser.csv.EditionParser;
import com.vituel.dndplayer.parser.csv.FeatParser;
import com.vituel.dndplayer.parser.csv.ItemParser;
import com.vituel.dndplayer.parser.csv.RaceParser;
import com.vituel.dndplayer.parser.csv.SkillParser;
import com.vituel.dndplayer.parser.csv.TempEffectParser;
import com.vituel.dndplayer.parser.exception.ParseEntityException;
import com.vituel.dndplayer.util.gui.LoaderObserver;

import java.io.IOException;

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
        loadTable(new EditionParser(ctx, "data/csv/editions.csv"), new EditionDao(ctx));
        loadTable(new BookParser(ctx, "data/csv/books.csv"), new BookDao(ctx));
        loadTable(new RaceParser(ctx, "data/csv/races.csv"), new RaceDao(ctx));
        loadTable(new ClassParser(ctx, "data/csv/classes.csv"), new ClassDao(ctx));
        loadTable(new ClassTraitParser(ctx, "data/csv/class_traits.csv"), new ClassTraitDao(ctx));
        loadTable(new ItemParser(ctx, "data/csv/items.csv"), new ItemDao(ctx));
        loadTable(new FeatParser(ctx, "data/csv/feats.csv"), new FeatDao(ctx));
        loadTable(new SkillParser(ctx, "data/csv/skills.csv"), new SkillDao(ctx));
        loadTable(new TempEffectParser(ctx, "data/csv/temp_effects.csv"), new TempEffectDao(ctx));
    }

    private <T extends AbstractEntity> void loadTable(AbstractCsvParser<T> parser, AbstractDao<T> dao) {
        try {
            observer.onStartLoadingFile(parser.getFilePath());
            while (parser.hasNext()) {
                loadEntity(parser, dao);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException exception when importing to " + dao.getClass().getSimpleName(), e);
        } finally {
            close(parser, dao);
        }
    }

    private <T extends AbstractEntity> void loadEntity(AbstractCsvParser<T> parser, AbstractDao<T> dao) {
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

    private <T> void close(AbstractCsvParser<T> parser, AbstractDao<T> dao) {
        dao.close();
        try {
            parser.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close parser.", e);
        }
    }

    public int getTotalFiles() {
        return 9;
    }

}
