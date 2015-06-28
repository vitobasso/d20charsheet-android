package com.vitobasso.d20charsheet.activity.summary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vitobasso.d20charsheet.R;
import com.vitobasso.d20charsheet.activity.abstraction.PagerFragment;
import com.vitobasso.d20charsheet.model.character.Attack;
import com.vitobasso.d20charsheet.model.character.AttackRound;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.ModifierSource;
import com.vitobasso.d20charsheet.model.effect.ModifierTarget;
import com.vitobasso.d20charsheet.util.app.ActivityUtil;
import com.vitobasso.d20charsheet.util.app.AppCommons;
import com.vitobasso.d20charsheet.util.business.AttackHelper;
import com.vitobasso.d20charsheet.util.gui.GuiInflater;

import java.text.MessageFormat;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vitobasso.d20charsheet.model.character.Attack.WeaponReference;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.AC;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CHA;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CON;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CONCEAL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.CRIT_MULT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DAMAGE;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DEX;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.DR;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.FORT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.HIT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.HP;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.INIT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.INT;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.MR;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.REFL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.SPEED;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.STR;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.WILL;
import static com.vitobasso.d20charsheet.model.effect.ModifierTarget.WIS;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.inflate;
import static com.vitobasso.d20charsheet.util.app.ActivityUtil.populateTextView;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryMainFragment extends PagerFragment<CharSummary, SummaryActivity> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.summary;
    }

    @Override
    protected void onPopulate() {
        populateMain();
        populateAttacks();
    }

    private void populateMain() {
        DamageGuiManager dmgGui = new DamageGuiManager(activity, data);

        String hpValue = MessageFormat.format("{0}/{1}", data.getCurrentHitPoints(), data.getHitPoints());
        setField(R.id.hit_points, R.string.hp, R.string.hit_points, hpValue, HP, dmgGui);
        String drValue = MessageFormat.format("{0}/-", data.getDamageReduction());
        setField(R.id.damage_reduction, R.string.dr, R.string.damage_reduction, drValue, DR, data.getDamageReduction());

        setField(R.id.strength, R.string.str, R.string.strength, formatAbility(data.getStrength(), ModifierSource.STR), STR);
        setField(R.id.dexterity, R.string.dex, R.string.dexterity, formatAbility(data.getDexterity(), ModifierSource.DEX), DEX);
        setField(R.id.constitution, R.string.con, R.string.constitution, formatAbility(data.getConstitution(), ModifierSource.CON), CON);
        setField(R.id.intelligence, R.string.int_ability, R.string.intelligence, formatAbility(data.getIntelligence(), ModifierSource.INT), INT);
        setField(R.id.wisdom, R.string.wis, R.string.wisdom, formatAbility(data.getWisdom(), ModifierSource.WIS), WIS);
        setField(R.id.charisma, R.string.cha, R.string.charisma, formatAbility(data.getCharisma(), ModifierSource.CHA), CHA);

        setField(R.id.armor_class, R.string.ac, R.string.armor_class, "" + data.getArmorClass(), AC);
        setField(R.id.spell_resistance, R.string.sr, R.string.spell_resistance, "" + data.getMagicResistance(), MR, data.getMagicResistance());

        setField(R.id.fortitude, R.string.empty, R.string.fort, "" + data.getFortitude(), FORT);
        setField(R.id.reflex, R.string.empty, R.string.refl, "" + data.getReflex(), REFL);
        setField(R.id.will, R.string.empty, R.string.will, "" + data.getWill(), WILL);

        String concealValue = MessageFormat.format("{0}%", data.getConcealment());
        setField(R.id.concealment, R.string.empty, R.string.conceal, concealValue, CONCEAL, data.getConcealment());
        setField(R.id.initiative, R.string.empty, R.string.init, "" + data.getInitiative(), INIT);
        setField(R.id.speed, R.string.empty, R.string.speed, "" + data.getSpeed(), SPEED);
    }

    private void populateAttacks() {
        ViewGroup atkParent = findView(R.id.attacks_root);
        atkParent.removeAllViews();
        for (int i = 0; i < data.getAttacks().size(); i++) {
            AttackRound attackRound = data.getAttacks().get(i);
            ViewGroup roundGroup = inflate(activity, atkParent, R.layout.summary_attack_round);
            populateTextView(roundGroup, R.id.name, attackRound.getName());

            //TODO decide if it's necessary to have independent damage or critical values
            //on different attacks of same weapon in the same attack round.
            //If so, group them based on an "equals" test.
            Map<Attack,String> grouped = AttackHelper.groupBonusByWeapon(attackRound);
            for (Attack attack : grouped.keySet()) {
                ViewGroup groupGroup = inflate(activity, roundGroup, R.layout.summary_attack_group);
                setField(groupGroup, R.id.attack, grouped.get(attack), HIT, i, attack.getWeaponReference());
                setField(groupGroup, R.id.damage, attack.getWeapon().getDamage().toString(), DAMAGE, i, attack.getWeaponReference());
                setField(groupGroup, R.id.critical, attack.getWeapon().getCritical().toString(), CRIT_MULT, i, attack.getWeaponReference());
            }
        }
    }

    private ViewGroup getGroup(int viewGroupId, final ModifierTarget target) {
        return getGroup(viewGroupId, target, null, null, null);
    }

    private ViewGroup getGroup(int viewGroupId, final ModifierTarget target, final GuiInflater guiInflater) {
        return getGroup(viewGroupId, target, null, null, guiInflater);
    }

    private ViewGroup getGroup(int viewGroupId, final ModifierTarget target, int ifZeroHide) {
        ViewGroup viewGroup = getGroup(viewGroupId, target, null, null, null);
        int visibility = ifZeroHide > 0 ? VISIBLE : GONE;
        viewGroup.setVisibility(visibility);
        return viewGroup;
    }

    private ViewGroup getGroup(int viewGroupId, final ModifierTarget target, final Integer attackIndex,
                               final WeaponReference weaponRef, final GuiInflater guiInflater) {
        ViewGroup viewGroup = findView(viewGroupId);
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBreakdownDialog(target, attackIndex, weaponRef, guiInflater);
            }
        });
        return viewGroup;
    }

    private void setField(ViewGroup parentView, int textViewId, String value, final ModifierTarget target, final Integer attackIndex,
                          final WeaponReference weaponRef) {
        setField(parentView, textViewId, value, target, attackIndex, weaponRef, null);
    }

    private void setField(ViewGroup parentView, int textViewId, String value,
                          final ModifierTarget target, final Integer attackIndex, final WeaponReference weaponRef,
                          final GuiInflater guiInflater) {
        TextView textView = populateTextView(parentView, textViewId, value);
        setFieldColor(textView, target);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBreakdownDialog(target, attackIndex, weaponRef, guiInflater);
            }
        });
    }

    private void setField(int groupViewId, int titleResId, int descResId, String value, ModifierTarget target, GuiInflater guiInflater) {
        ViewGroup parentView = getGroup(groupViewId, target, guiInflater);
        setFieldText(titleResId, descResId, value, target, parentView);
    }

    private void setField(int groupViewId, int titleResId, int descResId, String value, ModifierTarget target, int ifZeroHide) {
        ViewGroup parentView = getGroup(groupViewId, target, ifZeroHide);
        setFieldText(titleResId, descResId, value, target, parentView);
    }

    private void setField(int groupViewId, int titleResId, int descResId, String value, ModifierTarget target) {
        ViewGroup parentView = getGroup(groupViewId, target);
        setFieldText(titleResId, descResId, value, target, parentView);
    }

    private void setFieldText(int titleResId, int descResId, String value, ModifierTarget target, ViewGroup parentView) {
        String title = activity.getString(titleResId);
        String description = activity.getString(descResId);

        populateTextView(parentView, R.id.title, title);
        populateTextView(parentView, R.id.description, description);
        TextView valueView = populateTextView(parentView, R.id.value, value);
        setFieldColor(valueView, target);
    }

    private void showBreakdownDialog(ModifierTarget target, Integer attackIndex, WeaponReference weaponRef, GuiInflater guiInflater) {
        new BreakdownDialog(activity, data).buildDialog(target, null, attackIndex, weaponRef, guiInflater).show();
    }

    private void setFieldColor(TextView view, ModifierTarget target) {
        int color = getValueColor(activity, data, target);
        view.setTextColor(color);
    }

    public static int getValueColor(Context ctx, CharSummary charSummary, ModifierTarget target) {
        return new AppCommons(ctx).getValueColor(charSummary.getBase(), target, null);
    }

    private String formatAbility(int value, ModifierSource target) {
        int modifier = data.getAbilityModifier(target);
        String modifierStr = modifier < 0 ? "" + modifier : "+" + modifier;
        return MessageFormat.format("{0} ({1})", value, modifierStr);
    }

    //TODO move to PageFragment
    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }

}
