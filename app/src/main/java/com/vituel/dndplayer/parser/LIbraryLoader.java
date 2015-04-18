package com.vituel.dndplayer.parser;

import android.content.Context;

import com.vituel.dndplayer.dao.BookDao;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.dao.ClassTraitDao;
import com.vituel.dndplayer.dao.EditionDao;
import com.vituel.dndplayer.dao.FeatDao;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.dao.RaceTraitDao;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.dao.TempEffectDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.model.item.Item;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Edition;
import com.vituel.dndplayer.parser.csv.BookParser;
import com.vituel.dndplayer.parser.csv.EditionParser;
import com.vituel.dndplayer.parser.old_txt.ClassParser;
import com.vituel.dndplayer.parser.old_txt.ClassTraitParser;
import com.vituel.dndplayer.parser.old_txt.FeatParser;
import com.vituel.dndplayer.parser.old_txt.ItemParser;
import com.vituel.dndplayer.parser.old_txt.RaceParser;
import com.vituel.dndplayer.parser.old_txt.RaceTraitParser;
import com.vituel.dndplayer.parser.old_txt.SkillParser;
import com.vituel.dndplayer.parser.old_txt.TempEffectParser;

import java.util.List;
import java.util.Map;

/**
 * Created by Victor on 26/03/14.
 */
public class LibraryLoader {

    public static void loadDB(Context ctx) {

        List<Edition> editions = new EditionParser(ctx, "data/csv/editions.csv").loadFile();
        EditionDao editionDao = new EditionDao(ctx);
        editionDao.insert(editions);
        editionDao.close();

        List<Book> books = new BookParser(ctx, "data/csv/books.csv").loadFile();
        BookDao bookDao = new BookDao(ctx);
        bookDao.insert(books);
        bookDao.close();

        List<Race> races = new RaceParser(ctx, "data/races.txt").loadFile();
        RaceDao raceDao = new RaceDao(ctx);
        raceDao.save(races);

        Map<RaceTrait, Race> raceTraits = new RaceTraitParser(ctx, raceDao).loadFile("data/race_traits.txt");
        RaceTraitDao raceTraitDao = new RaceTraitDao(ctx);
        for (Map.Entry<RaceTrait, Race> entry : raceTraits.entrySet()) {
            raceTraitDao.save(entry.getValue().getId(), entry.getKey());
        }
        raceTraitDao.close();
        raceDao.close();

        List<Clazz> classes = new ClassParser(ctx, "data/classes.txt").loadFile();
        ClassDao classDao = new ClassDao(ctx);
        classDao.save(classes);

        Map<ClassTrait, Clazz> classTraits = new ClassTraitParser(ctx, classDao).loadFile("data/class_traits.txt");
        ClassTraitDao classTraitDao = new ClassTraitDao(ctx);
        for (Map.Entry<ClassTrait, Clazz> entry : classTraits.entrySet()) {
            classTraitDao.save(entry.getValue().getId(), entry.getKey());
        }
        classTraitDao.close();
        classDao.close();

        List<Feat> feats = new FeatParser(ctx, "data/feats.txt").loadFile();
        FeatDao featDao = new FeatDao(ctx);
        featDao.save(feats);
        featDao.close();

        List<Skill> skills = new SkillParser(ctx, "data/skills.txt").loadFile();
        SkillDao skillDao = new SkillDao(ctx);
        skillDao.save(skills);
        skillDao.close();

        List<Item> items = new ItemParser(ctx, "data/items.txt").loadFile();
        ItemDao itemDao = new ItemDao(ctx);
        itemDao.save(items);
        itemDao.close();

        List<TempEffect> tempEffects = new TempEffectParser(ctx, "data/temp_effects.txt").loadFile();
        TempEffectDao tempEffectDao = new TempEffectDao(ctx);
        tempEffectDao.save(tempEffects);
        tempEffectDao.close();
    }

}
