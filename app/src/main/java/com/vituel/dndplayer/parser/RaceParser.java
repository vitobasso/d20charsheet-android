package com.vituel.dndplayer.parser;

import android.content.Context;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Race;

import static com.vituel.dndplayer.model.ModifierTarget.*;

/**
 * Created by Victor on 26/03/14.
 */
public class RaceParser extends AbstractParser<Race> {

    public RaceParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Race parse(String line) {
        String split[] = line.split("\t");
        Race result = new Race();
        result.setName(split[0]);

        addMod(result, split, STR, 1);
        addMod(result, split, DEX, 2);
        addMod(result, split, CON, 3);
        addMod(result, split, INT, 4);
        addMod(result, split, WIS, 5);
        addMod(result, split, CHA, 6);
        addMod(result, split, SIZE, 7);
        addMod(result, split, SPEED, 8);

        return result;
    }

    private void addMod(Race result, String split[], ModifierTarget target, int index) {
        String str = split[index];
        if (str != null && !str.isEmpty()) {
            Modifier mod = new Modifier(target, Integer.valueOf(str), result);
            result.getModifiers().add(mod);
        }
    }

}
