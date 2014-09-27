package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.model.Trait;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class RaceTraitParser extends AbstractDependantParser<Trait, Race> {

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
    protected Trait parseDependant(String line) {
        String split[] = line.split("\t");

        Trait result = new Trait();
        result.setName(split[1]);
        result.setTraitType(Trait.Type.RACIAL);

        ModifierParser modParser = new ModifierParser(result);
        readModifier(modParser, split, 2, result);
        readModifier(modParser, split, 3, result);
        readModifier(modParser, split, 4, result);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Trait result) {
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
