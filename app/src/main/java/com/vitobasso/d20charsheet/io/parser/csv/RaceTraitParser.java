package com.vitobasso.d20charsheet.io.parser.csv;

import android.content.Context;

import com.vitobasso.d20charsheet.io.importer.RulesImporter;
import com.vitobasso.d20charsheet.io.parser.exception.ParseFieldException;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.RaceTrait;

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
