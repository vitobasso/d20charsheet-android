package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.model.Feat;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.Race;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class RaceTraitParser extends AbstractDependantParser<Feat, Race> {

    public RaceTraitParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Race parseOwner(String line) {
        String split[] = line.split("\t");

        RaceDao raceDao = new RaceDao(ctx);
        Race race = raceDao.findByName(split[0]);
        raceDao.close();

        return race;
    }

    @Override
    protected Feat parseDependant(String line) {
        String split[] = line.split("\t");

        Feat result = new Feat();
        result.setName(split[1]);
        result.setTraitType(Feat.Type.RACIAL);

        ModifierParser modParser = new ModifierParser(result);
        readModifier(modParser, split, 2, result);
        readModifier(modParser, split, 3, result);
        readModifier(modParser, split, 4, result);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Feat result) {
        if (split.length > index) {
            try {
                String str = split[index];
                if (str != null && !str.isEmpty()) {
                    Modifier mod = parser.parse(str);
                    result.addModifier(mod);
                }
            } catch (ParseException e) {
                Log.w(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
