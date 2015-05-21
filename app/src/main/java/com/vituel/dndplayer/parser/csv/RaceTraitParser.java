package com.vituel.dndplayer.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;
import com.vituel.dndplayer.parser.exception.ParseFieldException;

import java.util.Map;

/**
 * Created by Victor on 21/04/2015.
 */
public class RaceTraitParser extends AbstractEffectParser<RaceTrait> {

    public RaceTraitParser(Context ctx, String path, Map<String, String> skillNameMap) {
        super(ctx, path, skillNameMap);
    }

    @Override
    protected RaceTrait parse(String[] line, RaceTrait result) throws ParseFieldException {
        readEffect(line, result);

        Race race = new Race();
        race.setId(readInt(line, "race_id"));
        result.setRace(race);

        return result;
    }

    @Override
    protected RaceTrait newInstance(String[] split) {
        return new RaceTrait();
    }

}
