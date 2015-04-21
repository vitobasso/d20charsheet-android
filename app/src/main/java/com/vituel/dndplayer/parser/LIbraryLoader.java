package com.vituel.dndplayer.parser;

import android.content.Context;

import com.vituel.dndplayer.dao.AbstractDao;
import com.vituel.dndplayer.dao.BookDao;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.dao.ClassTraitDao;
import com.vituel.dndplayer.dao.EditionDao;
import com.vituel.dndplayer.dao.FeatDao;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.dao.TempEffectDao;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;
import com.vituel.dndplayer.parser.csv.BookParser;
import com.vituel.dndplayer.parser.csv.ClassParser;
import com.vituel.dndplayer.parser.csv.ClassTraitParser;
import com.vituel.dndplayer.parser.csv.EditionParser;
import com.vituel.dndplayer.parser.csv.FeatParser;
import com.vituel.dndplayer.parser.csv.ItemParser;
import com.vituel.dndplayer.parser.csv.RaceParser;

import java.util.List;

/**
 * Created by Victor on 26/03/14.
 */
public class LibraryLoader {

    public static void loadDB(Context ctx) {

        loadTable(new EditionParser(ctx, "data/csv/editions.csv"), new EditionDao(ctx));
        loadTable(new BookParser(ctx, "data/csv/books.csv"), new BookDao(ctx));
        loadTable(new RaceParser(ctx, "data/csv/races.csv"), new RaceDao(ctx));
        loadTable(new ClassParser(ctx, "data/csv/classes_new.csv"), new ClassDao(ctx));
        loadTable(new ClassTraitParser(ctx, "data/csv/class_traits.csv"), new ClassTraitDao(ctx));
        loadTable(new ItemParser(ctx, "data/csv/items.csv"), new ItemDao(ctx));
        loadTable(new FeatParser(ctx, "data/csv/feats.csv"), new FeatDao(ctx));


        // OLD

        List<Skill> skills = new com.vituel.dndplayer.parser.old_txt.SkillParser(ctx, "data/skills.txt").loadFile();
        SkillDao skillDao = new SkillDao(ctx);
        skillDao.save(skills);
        skillDao.close();

        List<TempEffect> tempEffects = new com.vituel.dndplayer.parser.old_txt.TempEffectParser(ctx, "data/temp_effects.txt").loadFile();
        TempEffectDao tempEffectDao = new TempEffectDao(ctx);
        tempEffectDao.save(tempEffects);
        tempEffectDao.close();
    }

    private static <T> void loadTable(AbstractSimpleParser<T> parser, AbstractDao<T> dao) {
        List<T> entities = parser.loadFile();
        dao.insert(entities);
        dao.close();
    }

}
