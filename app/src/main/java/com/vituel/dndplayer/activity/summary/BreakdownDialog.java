package com.vituel.dndplayer.activity.summary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.model.AttackRound;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.Character;
import com.vituel.dndplayer.model.ClassLevel;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.model.Race;
import com.vituel.dndplayer.model.WeaponProperties;
import com.vituel.dndplayer.util.AttackUtil;
import com.vituel.dndplayer.util.ModifierStringConverter;
import com.vituel.dndplayer.util.font.FontUtil;
import com.vituel.dndplayer.util.gui.GuiInflater;

import static android.view.View.GONE;
import static com.vituel.dndplayer.model.Attack.WeaponReferenceType;
import static com.vituel.dndplayer.util.ActivityUtil.findView;
import static com.vituel.dndplayer.util.font.FontUtil.MAIN_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.setFontRecursively;

/**
 * Created by Victor on 20/03/14.
 */
public class BreakdownDialog {

    Activity activity;
    com.vituel.dndplayer.model.Character character;

    public BreakdownDialog(Activity activity, Character character) {
        this.activity = activity;
        this.character = character;
    }

    public Dialog buildDialog(ModifierTarget target, String variation) {
        return buildDialog(target, variation, null, null, null);
    }

    public Dialog buildDialog(ModifierTarget target, String variation, Integer attackRoundIndex, WeaponReferenceType weaponRef, GuiInflater delegate) {
        BreakdownDialogInflater bdi = new BreakdownDialogInflater(activity, character, target, variation);
        ViewGroup rootView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.summary_main_breakdown, null);
        CharBase base = character.getBase();

        //title
        ModifierStringConverter modConv = new ModifierStringConverter(activity);
        CharSequence title = modConv.getTargetShort(target, variation);
        TextView titleView = findView(rootView, R.id.title);
        titleView.setText(FontUtil.toBold(title));


        //BASE
        {
            ViewGroup baseGroup = findView(rootView, R.id.base);

            //base
            int count = bdi.appendRows(baseGroup, character.getBaseModifiers(), "Base");

            //attack specific
            if (attackRoundIndex != null) {
                AttackRound attackRound = character.getAttacks().get(attackRoundIndex);
                if (target == ModifierTarget.HIT) {
                    count += bdi.appendRows(baseGroup, attackRound.getBaseModifiers(), attackRound.getName());
                } else if (weaponRef != null) {
                    WeaponProperties weapon = AttackUtil.getRepresentativeWeapon(attackRound, weaponRef);
                    count += bdi.appendRows(baseGroup, weapon.getAsModifiers(), weapon.getName());
                }
            }

            //size
            count += bdi.appendRows(baseGroup, character.getSizeModifiers(), "Size");

            //attributes
            count += bdi.appendRows(baseGroup, character.getAbilityModifiers());

            //feats
            count += bdi.appendRows(rootView, base.getFeats(), false);

            if (count == 0) {
                baseGroup.setVisibility(GONE);
            }
        }


        //RACE
        {
            View raceTitle = findView(rootView, R.id.raceTitle);
            ViewGroup raceGroup = findView(rootView, R.id.race);

            Race race = base.getRace();
            int count = bdi.appendRows(raceGroup, race, false);
            if (race != null) {
                count += bdi.appendRows(raceGroup, race.getTraits(), race.getName());
            }

            if (count == 0) {
                raceTitle.setVisibility(GONE);
                raceGroup.setVisibility(GONE);
            }
        }


        //CLASS
        {
            View classTitle = findView(rootView, R.id.classTitle);
            ViewGroup classGroup = findView(rootView, R.id.clazz);

            int count = bdi.appendRows(classGroup, base.getClassLevels(), false);
            for (ClassLevel classLevel : base.getClassLevels()) {
                String source = classLevel.getClazz().getName() + " " + classLevel.getLevel();
                count += bdi.appendRows(classGroup, classLevel.getTraits(), source);
            }

            if (count == 0) {
                classTitle.setVisibility(GONE);
                classGroup.setVisibility(GONE);
            }
        }


        //EQUIP
        {
            View equipTitle = findView(rootView, R.id.equipTitle);
            ViewGroup equipGroup = findView(rootView, R.id.equip);

            int count = bdi.appendRows(equipGroup, base.getEquipmentItems(), false);

            if (count == 0) {
                equipTitle.setVisibility(GONE);
                equipGroup.setVisibility(GONE);
            }
        }


        //TEMPORARY EFFECTS
        {
            View tempTitle = findView(rootView, R.id.tempTitle);
            ViewGroup tempGroup = findView(rootView, R.id.temp);

            int count = bdi.appendRows(tempGroup, base.getActiveTempEffects(), true);

            if (count == 0) {
                tempTitle.setVisibility(GONE);
                tempGroup.setVisibility(GONE);
            }

        }


        //delegate additional GUI
        if (delegate != null) {
            delegate.inflate(rootView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(rootView);
        setFontRecursively(activity, rootView, MAIN_FONT);
        return builder.create();
    }

}
