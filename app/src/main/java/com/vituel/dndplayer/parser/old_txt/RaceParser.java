package com.vituel.dndplayer.parser.old_txt;

import android.content.Context;

import com.vituel.dndplayer.model.Effect;
import com.vituel.dndplayer.model.Modifier;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.parser.csv.AbstractSimpleParser;

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
public class RaceParser extends AbstractSimpleParser<Race> {

    public RaceParser(Context ctx) {
        super(ctx);
    }

    @Override
    protected Race parse(String[] line) {
        Race result = new Race();
        result.setName(line[0]);

        Effect effect = new Effect();
        effect.setSourceName(result.getName());
        result.setEffect(effect);

        addMod(effect, line, STR, 1);
        addMod(effect, line, DEX, 2);
        addMod(effect, line, CON, 3);
        addMod(effect, line, INT, 4);
        addMod(effect, line, WIS, 5);
        addMod(effect, line, CHA, 6);
        addMod(effect, line, SIZE, 7);
        addMod(effect, line, SPEED, 8);

        return result;
    }

    private void addMod(Effect effect, String split[], ModifierTarget target, int index) {
        String str = split[index];
        if (str != null && !str.isEmpty()) {
            Modifier mod = new Modifier(target, Integer.valueOf(str));
            effect.addModifier(mod);
        }
    }

}
