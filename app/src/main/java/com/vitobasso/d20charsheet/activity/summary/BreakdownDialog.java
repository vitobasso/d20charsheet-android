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
import com.vitobasso.d20charsheet.util.business.ModifierCreator;
import com.vitobasso.d20charsheet.util.font.FontUtil;
import com.vitobasso.d20charsheet.util.gui.EmptyGuiInflater;
import com.vitobasso.d20charsheet.util.gui.GuiInflater;
import com.vitobasso.d20charsheet.util.i18n.ModifierStringConverter;

import static android.view.View.GONE;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.findView;

/**
 * Created by Victor on 20/03/14.
 */
public class BreakdownDialog {

    private Activity activity;
    private CharSummary summary;
    private ModifierCreator modifierCreator;
    private CharBase base;

    private BreakdownDialogInflater inflater;
    private ViewGroup rootView;
    private GuiInflater delegateInflater;

    public BreakdownDialog(Activity activity, CharSummary summary) {
        this.activity = activity;
        this.summary = summary;
        this.base = summary.getBase();
        this.modifierCreator = new ModifierCreator(activity, summary);
    }

    public Dialog buildDialog(ModifierTarget target, String variation) {
        return buildDialog(target, variation, null, null, null);
    }

    public Dialog buildDialog(ModifierTarget target, String variation, Integer attackRoundIndex, Attack.WeaponReference weaponReference, GuiInflater delegate) {
        init(target, variation, delegate);

        setTitle(target, variation);
        inflateBase(target, attackRoundIndex, weaponReference);
        inflateRace();
        inflateClass();
        inflateEquip();
        inflateTempEffects();

        delegateInflater.inflate(rootView);
        return buildDialog();
    }

    private void init(ModifierTarget target, String variation, GuiInflater delegate) {
        this.inflater = new BreakdownDialogInflater(activity, summary, target, variation);
        this.rootView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.summary_breakdown, null);
        this.delegateInflater = delegate != null ? delegate : new EmptyGuiInflater();
    }

    private void setTitle(ModifierTarget target, String variation) {
        ModifierStringConverter modConv = new ModifierStringConverter(activity);
        CharSequence title = modConv.getTargetLong(target, variation);
        TextView titleView = findView(rootView, R.id.title);
        titleView.setText(FontUtil.toBold(title));
    }

    private void inflateBase(ModifierTarget target, Integer attackRoundIndex, Attack.WeaponReference weaponReference) {
        ViewGroup baseGroup = findView(rootView, R.id.base);

        //base
        int count = inflater.appendRows(baseGroup, modifierCreator.createBaseModifiers(), "Base");

        //attack specific
        count += inflateAttackSpecific(target, attackRoundIndex, weaponReference, baseGroup);

        //size
        count += inflater.appendRows(baseGroup, modifierCreator.createSizeModifiers(), "Size");

        //attributes
        count += inflater.appendRows(baseGroup, modifierCreator.createAbilityModifiers());

        //feats
        count += inflater.appendRows(rootView, base.getFeats());

        hideIfEmpty(null, baseGroup, count);
    }

    private int inflateAttackSpecific(ModifierTarget target, Integer attackRoundIndex, Attack.WeaponReference weaponReference, ViewGroup baseGroup) {
        int count = 0;
        if (attackRoundIndex != null) {
            AttackRound attackRound = summary.getAttacks().get(attackRoundIndex);
            if (target == ModifierTarget.HIT) {
                count += inflater.appendRows(baseGroup, attackRound.getBaseModifiers(), attackRound.getName());
            } else if (weaponReference != null) {
                WeaponProperties weapon = base.getWeapon(weaponReference);
                count += inflater.appendRows(baseGroup, weapon.getAsModifiers(), weapon.getName());
            }
        }
        return count;
    }

    private void inflateRace() {
        View raceTitle = findView(rootView, R.id.raceTitle);
        ViewGroup raceGroup = findView(rootView, R.id.race);

        Race race = base.getRace();
        int count = inflater.appendRows(raceGroup, race);
        if (race != null) {
            count += inflater.appendRows(raceGroup, race.getTraits(), race.getName());
        }

        hideIfEmpty(raceTitle, raceGroup, count);
    }

    private void inflateClass() {
        View classTitle = findView(rootView, R.id.classTitle);
        ViewGroup classGroup = findView(rootView, R.id.clazz);

        int count = inflater.appendRows(classGroup, base.getClassLevels());
        for (ClassLevel classLevel : base.getClassLevels()) {
            String source = classLevel.getClazz().getName() + " " + classLevel.getLevel();
            count += inflater.appendRows(classGroup, classLevel.getTraits(), source);
        }

        hideIfEmpty(classTitle, classGroup, count);
    }

    private void inflateEquip() {
        View equipTitle = findView(rootView, R.id.equipTitle);
        ViewGroup equipGroup = findView(rootView, R.id.equip);

        int count = inflater.appendRows(equipGroup, base.getEquipmentItems());

        hideIfEmpty(equipTitle, equipGroup, count);
    }

    private void inflateTempEffects() {
        View tempTitle = findView(rootView, R.id.tempTitle);
        ViewGroup tempGroup = findView(rootView, R.id.temp);

        int count = inflater.appendRowsColored(tempGroup, base.getActiveTempEffects());

        hideIfEmpty(tempTitle, tempGroup, count);
    }

    private Dialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(rootView);
        new FontUtil(activity).setFontRecursively(rootView);
        return builder.create();
    }

    private void hideIfEmpty(View titleView, ViewGroup groupView, int count) {
        if (count == 0) {
            hideView(titleView);
            hideView(groupView);
        }
    }

    private void hideView(View view) {
        if (view != null) {
            view.setVisibility(GONE);
        }
    }

}
