package com.vituel.dndplayer.io.parser.csv;

import android.content.Context;

import com.vituel.dndplayer.io.parser.RulesImporter;
import com.vituel.dndplayer.io.parser.exception.ParseFieldException;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.RaceTrait;

import java.io.File;

/**
 * Created by Victor on 21/04/2015.
 */
public class RaceTraitParser extends AbstractEffectParser<RaceTrait> {

    public RaceTraitParser(Context ctx, File file, RulesImporter.ParserCache loadingCache) {
        super(ctx, file, loadingCache);
    }

    @Override
    protected RaceTrait parse(String[] line, RaceTrait result) throws ParseFieldException {
        readEffect(line, result);

        Race race = new Race();
        race.setId(readInt(line, "race_id"));
        result.setBook(readRulebook(line));
        result.setRace(race);

        return result;
    }

    @Override
    protected RaceTrait newInstance(String[] split) {
        return new RaceTrait();
    }

}
