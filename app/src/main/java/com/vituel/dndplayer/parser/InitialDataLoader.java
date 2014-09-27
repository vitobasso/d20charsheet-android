package com.vituel.dndplayer.parser;

import android.content.Context;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.model.Clazz;
import com.vituel.dndplayer.model.Item;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.model.Skill;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.model.TempEffect;
import com.vituel.dndplayer.dao.TempEffectDao;
import com.vituel.dndplayer.model.Trait;
import com.vituel.dndplayer.dao.TraitDao;
import com.vituel.dndplayer.dao.TraitLinkDao;

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
        raceDao.close();

        Map<Trait, Race> raceTraits = new RaceTraitParser(ctx).loadFile("data/race_traits.txt");
        TraitDao traitDao = new TraitDao(ctx);
        traitDao.save(raceTraits.keySet());
        traitDao.close();
        TraitLinkDao traitLinkDao = new TraitLinkDao(ctx);
        traitLinkDao.saveForRaces(raceTraits);
        traitLinkDao.close();

        List<Clazz> classes = new ClassParser(ctx).loadFile("data/classes.txt");
        ClassDao classDao = new ClassDao(ctx);
        classDao.save(classes);
        classDao.close();

        Map<Trait, Clazz> classTraits = new ClassTraitParser(ctx).loadFile("data/class_traits.txt");
        traitDao = new TraitDao(ctx);
        traitDao.save(classTraits.keySet());
        traitDao.close();
        traitLinkDao = new TraitLinkDao(ctx);
        traitLinkDao.saveForClasses(classTraits);

        List<Trait> feats = new FeatParser(ctx).loadFile("data/feats.txt");
        new TraitDao(ctx).save(feats);

        List<Skill> skills = new SkillParser(ctx).loadFile("data/skills.txt");
        new SkillDao(ctx).save(skills);

        List<Item> items = new ItemParser(ctx).loadFile("data/items.txt");
        new ItemDao(ctx).save(items);

        List<TempEffect> tempEffects = new TempEffectParser(ctx).loadFile("data/temp_effects.txt");
        new TempEffectDao(ctx).save(tempEffects);
    }

}
