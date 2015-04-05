package com.vituel.dndplayer.parser;

import android.content.Context;

import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.dao.ClassTraitDao;
import com.vituel.dndplayer.dao.FeatDao;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.dao.RaceTraitDao;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.dao.TempEffectDao;
import com.vituel.dndplayer.model.ClassTrait;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Item;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.model.TempEffect;

import java.util.List;
import java.util.Map;

/**
 * Created by Victor on 26/03/14.
 */
public class InitialDataLoader {

    public static void loadDB(Context ctx){

        List<Race> races = new RaceParser(ctx).loadFile("data/races.txt");
        RaceDao raceDao = new RaceDao(ctx);
        raceDao.save(races);

        Map<RaceTrait, Race> raceTraits = new RaceTraitParser(ctx, raceDao).loadFile("data/race_traits.txt");
        RaceTraitDao raceTraitDao = new RaceTraitDao(ctx);
        for (Map.Entry<RaceTrait, Race> entry : raceTraits.entrySet()) {
            raceTraitDao.save(entry.getValue().getId(), entry.getKey());
        }
        raceTraitDao.close();
        raceDao.close();

        List<Clazz> classes = new ClassParser(ctx).loadFile("data/classes.txt");
        ClassDao classDao = new ClassDao(ctx);
        classDao.save(classes);

        Map<ClassTrait, Clazz> classTraits = new ClassTraitParser(ctx, classDao).loadFile("data/class_traits.txt");
        ClassTraitDao classTraitDao = new ClassTraitDao(ctx);
        for (Map.Entry<ClassTrait, Clazz> entry : classTraits.entrySet()) {
            classTraitDao.save(entry.getValue().getId(), entry.getKey());
        }
        classTraitDao.close();
        classDao.close();

        List<Feat> feats = new FeatParser(ctx).loadFile("data/feats.txt");
        new FeatDao(ctx).save(feats);

        List<Skill> skills = new SkillParser(ctx).loadFile("data/skills.txt");
        new SkillDao(ctx).save(skills);

        List<Item> items = new ItemParser(ctx).loadFile("data/items.txt");
        new ItemDao(ctx).save(items);

        List<TempEffect> tempEffects = new TempEffectParser(ctx).loadFile("data/temp_effects.txt");
        new TempEffectDao(ctx).save(tempEffects);
    }

}
