package com.vituel.dndplayer.parser;

import android.content.Context;
import android.util.Log;

import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;

import java.text.ParseException;

/**
 * Created by Victor on 26/03/14.
 */
public class RaceTraitParser extends AbstractDependantParser<RaceTrait, Race> {

    private RaceDao raceDao = new RaceDao(ctx);

    public RaceTraitParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Race parseOwner(String line) {
        String split[] = line.split("\t");

        Race race = raceDao.findByName(split[0]);
        raceDao.close();

        return race;
    }

    @Override
    protected RaceTrait parseDependant(String line) {
        String split[] = line.split("\t");

        RaceTrait result = new RaceTrait();
        result.setName(split[1]);

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        ModifierParser modParser = new ModifierParser();
        readModifier(modParser, split, 2, effect);
        readModifier(modParser, split, 3, effect);
        readModifier(modParser, split, 4, effect);

        return result;
    }

    protected void readModifier(ModifierParser parser, String[] split, int index, Effect effect) {
        if (split.length > index) {
            try {
                String str = split[index];
                if (str != null && !str.isEmpty()) {
                    Modifier mod = parser.parse(str);
                    effect.addModifier(mod);
                }
            } catch (ParseException e) {
                Log.w(this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
