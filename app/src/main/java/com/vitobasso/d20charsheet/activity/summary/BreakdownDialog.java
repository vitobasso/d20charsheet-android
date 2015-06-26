package com.vitobasso.d20charsheet.activity.summary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.model.ClassLevel;
import com.vitobasso.d20charsheet.model.Race;
import com.vitobasso.d20charsheet.model.character.Attack;
import com.vitobasso.d20charsheet.model.character.AttackRound;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;
import com.vitobasso.d20charsheet.util.font.FontUtil;
import com.vitobasso.d20charsheet.util.gui.GuiInflater;
import com.vitobasso.d20charsheet.util.i18n.ModifierStringConverter;

import static android.view.View.GONE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;

/**
 * Created by Victor on 20/03/14.
 */
public class BreakdownDialog {

    private Activity activity;
    private CharSummary charSummary;

    public BreakdownDialog(Activity activity, CharSummary charSummary) {
        this.activity = activity;
        this.charSummary = charSummary;
    }

    public Dialog buildDialog(ModifierTarget target, String variation) {
        return buildDialog(target, variation, null, null, null);
    }

    public Dialog buildDialog(ModifierTarget target, String variation, Integer attackRoundIndex, Attack.WeaponReference weaponReference, GuiInflater delegate) {
        BreakdownDialogInflater inflater = new BreakdownDialogInflater(activity, charSummary, target, variation);
        ViewGroup rootView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.summary_breakdown, null);
        CharBase base = charSummary.getBase();

        //title
        ModifierStringConverter modConv = new ModifierStringConverter(activity);
        CharSequence title = modConv.getTargetLong(target, variation);
        TextView titleView = findView(rootView, R.id.title);
        titleView.setText(FontUtil.toBold(title));


        //BASE
        {
            ViewGroup baseGroup = findView(rootView, R.id.base);

            //base
            int count = inflater.appendRows(baseGroup, charSummary.createBaseModifiers(), "Base");

            //attack specific
            if (attackRoundIndex != null) {
                AttackRound attackRound = charSummary.getAttacks().get(attackRoundIndex);
                if (target == ModifierTarget.HIT) {
                    count += inflater.appendRows(baseGroup, attackRound.getBaseModifiers(), attackRound.getName());
                } else if (weaponReference != null) {
                    WeaponProperties weapon = base.getWeapon(weaponReference);
                    count += inflater.appendRows(baseGroup, weapon.getAsModifiers(), weapon.getName());
                }
            }

            //size
            count += inflater.appendRows(baseGroup, charSummary.createSizeModifiers(), "Size");

            //attributes
            count += inflater.appendRows(baseGroup, charSummary.createAbilityModifiers());

            //feats
            count += inflater.appendRows(rootView, base.getFeats(), false);

            if (count == 0) {
                baseGroup.setVisibility(GONE);
            }
        }


        //RACE
        {
            View raceTitle = findView(rootView, R.id.raceTitle);
            ViewGroup raceGroup = findView(rootView, R.id.race);

            Race race = base.getRace();
            int count = inflater.appendRows(raceGroup, race, false);
            if (race != null) {
                count += inflater.appendRows(raceGroup, race.getTraits(), race.getName());
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

            int count = inflater.appendRows(classGroup, base.getClassLevels(), false);
            for (ClassLevel classLevel : base.getClassLevels()) {
                String source = classLevel.getClazz().getName() + " " + classLevel.getLevel();
                count += inflater.appendRows(classGroup, classLevel.getTraits(), source);
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

            int count = inflater.appendRows(equipGroup, base.getEquipmentItems(), false);

            if (count == 0) {
                equipTitle.setVisibility(GONE);
                equipGroup.setVisibility(GONE);
            }
        }


        //TEMPORARY EFFECTS
        {
            View tempTitle = findView(rootView, R.id.tempTitle);
            ViewGroup tempGroup = findView(rootView, R.id.temp);

            int count = inflater.appendRows(tempGroup, base.getActiveTempEffects(), true);

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
        new FontUtil(activity).setFontRecursively(rootView);
        return builder.create();
    }

}
