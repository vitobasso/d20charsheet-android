package com.vituel.dndplayer.parser;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.vituel.dndplayer.dao.AbstractDao;
import com.vituel.dndplayer.dao.BookDao;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.dao.ClassTraitDao;
import com.vituel.dndplayer.dao.EditionDao;
import com.vituel.dndplayer.dao.FeatDao;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;
import com.vituel.dndplayer.parser.csv.BookParser;
import com.vituel.dndplayer.parser.csv.ClassParser;
import com.vituel.dndplayer.parser.csv.ClassTraitParser;
import com.vituel.dndplayer.parser.csv.EditionParser;
import com.vituel.dndplayer.parser.csv.FeatParser;
import com.vituel.dndplayer.parser.csv.ItemParser;
import com.vituel.dndplayer.parser.csv.RaceParser;
import com.vituel.dndplayer.parser.exception.ParseEntityException;

import java.io.IOException;

/**
 * Created by Victor on 26/03/14.
 */
public class LibraryLoader {

    public static final String TAG = LibraryLoader.class.getSimpleName();

    public static void loadDB(Context ctx) {

        loadTable(new EditionParser(ctx, "data/csv/editions.csv"), new EditionDao(ctx));
        loadTable(new BookParser(ctx, "data/csv/books.csv"), new BookDao(ctx));
        loadTable(new RaceParser(ctx, "data/csv/races.csv"), new RaceDao(ctx));
        loadTable(new ClassParser(ctx, "data/csv/classes_new.csv"), new ClassDao(ctx));
        loadTable(new ClassTraitParser(ctx, "data/csv/class_traits.csv"), new ClassTraitDao(ctx));
        loadTable(new ItemParser(ctx, "data/csv/items.csv"), new ItemDao(ctx));
        loadTable(new FeatParser(ctx, "data/csv/feats.csv"), new FeatDao(ctx));


        // OLD

//        List<Skill> skills = new com.vituel.dndplayer.parser.old_txt.SkillParser(ctx, "data/skills.txt").read();
//        SkillDao skillDao = new SkillDao(ctx);
//        skillDao.save(skills);
//        skillDao.close();
//
//        List<TempEffect> tempEffects = new com.vituel.dndplayer.parser.old_txt.TempEffectParser(ctx, "data/temp_effects.txt").read();
//        TempEffectDao tempEffectDao = new TempEffectDao(ctx);
//        tempEffectDao.save(tempEffects);
//        tempEffectDao.close();
    }

    private static <T> void loadTable(AbstractSimpleParser<T> parser, AbstractDao<T> dao) {
        try {
            while (parser.hasNext()) {
                loadEntity(parser, dao);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException exception when importing to " + dao.getClass().getSimpleName(), e);
        } finally {
            close(parser, dao);
        }
    }

    private static <T> void loadEntity(AbstractSimpleParser<T> parser, AbstractDao<T> dao) {
        try {
            T entity = parser.next();
            dao.insert(entity);
        } catch (ParseEntityException | SQLException e) {
            Log.w(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception when importing to " + dao.getClass().getSimpleName(), e);
        }
    }

    private static <T> void close(AbstractSimpleParser<T> parser, AbstractDao<T> dao) {
        dao.close();
        try {
            parser.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close parser.", e);
        }
    }


}
