package com.vituel.dndplayer.parser;

import android.content.Context;

import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Race;

import static com.vituel.dndplayer.model.ModifierTarget.CHA;
import static com.vituel.dndplayer.model.ModifierTarget.CON;
import static com.vituel.dndplayer.model.ModifierTarget.DEX;
import static com.vituel.dndplayer.model.ModifierTarget.INT;
import static com.vituel.dndplayer.model.ModifierTarget.SIZE;
import static com.vituel.dndplayer.model.ModifierTarget.SPEED;
import static com.vituel.dndplayer.model.ModifierTarget.STR;
import static com.vituel.dndplayer.model.ModifierTarget.WIS;

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

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        addMod(effect, split, STR, 1);
        addMod(effect, split, DEX, 2);
        addMod(effect, split, CON, 3);
        addMod(effect, split, INT, 4);
        addMod(effect, split, WIS, 5);
        addMod(effect, split, CHA, 6);
        addMod(effect, split, SIZE, 7);
        addMod(effect, split, SPEED, 8);

        return result;
    }

    private void addMod(Effect effect, String split[], ModifierTarget target, int index) {
        String str = split[index];
        if (str != null && !str.isEmpty()) {
            Modifier mod = new Modifier(target, Integer.valueOf(str));
            effect.getModifiers().add(mod);
        }
    }

}
