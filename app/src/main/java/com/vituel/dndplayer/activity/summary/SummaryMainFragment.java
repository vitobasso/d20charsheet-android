package com.vituel.dndplayer.activity.summary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vituel.dndplayer.R;
import com.vituel.dndplayer.activity.abstraction.PagerFragment;
import com.vituel.dndplayer.activity.edit_char.EditCharActivity;
import com.vituel.dndplayer.activity.edit_char.EditCharPagerAdapter;
import com.vituel.dndplayer.model.Attack;
import com.vituel.dndplayer.model.AttackRound;
import com.vituel.dndplayer.model.CharBase;
import com.vituel.dndplayer.model.CharSummary;
import com.vituel.dndplayer.model.ModifierSource;
import com.vituel.dndplayer.model.ModifierTarget;
import com.vituel.dndplayer.util.ActivityUtil;
import com.vituel.dndplayer.util.AppCommons;
import com.vituel.dndplayer.util.AttackUtil;
import com.vituel.dndplayer.util.font.FontUtil;
import com.vituel.dndplayer.util.gui.GuiInflater;
import com.vituel.dndplayer.util.gui.RecursiveViewCaller;

import java.text.MessageFormat;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vituel.dndplayer.model.Attack.WeaponReference;
import static com.vituel.dndplayer.model.ModifierTarget.AC;
import static com.vituel.dndplayer.model.ModifierTarget.CHA;
import static com.vituel.dndplayer.model.ModifierTarget.CON;
import static com.vituel.dndplayer.model.ModifierTarget.CONCEAL;
import static com.vituel.dndplayer.model.ModifierTarget.CRIT_MULT;
import static com.vituel.dndplayer.model.ModifierTarget.DAMAGE;
import static com.vituel.dndplayer.model.ModifierTarget.DEX;
import static com.vituel.dndplayer.model.ModifierTarget.DR;
import static com.vituel.dndplayer.model.ModifierTarget.FORT;
import static com.vituel.dndplayer.model.ModifierTarget.HIT;
import static com.vituel.dndplayer.model.ModifierTarget.HP;
import static com.vituel.dndplayer.model.ModifierTarget.INIT;
import static com.vituel.dndplayer.model.ModifierTarget.INT;
import static com.vituel.dndplayer.model.ModifierTarget.MR;
import static com.vituel.dndplayer.model.ModifierTarget.REFL;
import static com.vituel.dndplayer.model.ModifierTarget.SPEED;
import static com.vituel.dndplayer.model.ModifierTarget.STR;
import static com.vituel.dndplayer.model.ModifierTarget.WILL;
import static com.vituel.dndplayer.model.ModifierTarget.WIS;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_EDITED;
import static com.vituel.dndplayer.util.ActivityUtil.EXTRA_PAGE;
import static com.vituel.dndplayer.util.ActivityUtil.REQUEST_EDIT;
import static com.vituel.dndplayer.util.ActivityUtil.inflate;
import static com.vituel.dndplayer.util.ActivityUtil.populateTextView;
import static com.vituel.dndplayer.util.font.FontUtil.BOLDER_FONT;
import static com.vituel.dndplayer.util.font.FontUtil.BOLD_FONT;

/**
 * Created by Victor on 21/03/14.
 */
public class SummaryMainFragment extends PagerFragment<CharSummary, SummaryActivity> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.summary_main;
    }

    @Override
    protected void onPopulate() {
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
        setField(R.id.concealment, R.string.empty, R.string.conceal_short, concealValue, CONCEAL, data.getConcealment());

        setField(R.id.initiative, R.string.empty, R.string.init, "" + data.getInitiative(), INIT);
        setField(R.id.speed, R.string.empty, R.string.speed_short, "" + data.getSpeed(), SPEED);

        //attacks
        ViewGroup atkParent = findView(R.id.attacks_root);
        atkParent.removeAllViews();
        for (int i = 0; i < data.getAttacks().size(); i++) {
            AttackRound attackRound = data.getAttacks().get(i);
            ViewGroup roundGroup = inflate(activity, atkParent, R.layout.summary_main_attack_round);
            populateTextView(roundGroup, R.id.name, attackRound.getName());

            //TODO decide if it's necessary to have independent damage or critical values
            //on different attacks of same weapon in the same attack round.
            //If so, group them based on an "equals" test.
            Map<Attack,String> grouped = AttackUtil.groupBonusByWeapon(attackRound.getAttacks());
            for (Attack attack : grouped.keySet()) {
                ViewGroup groupGroup = inflate(activity, roundGroup, R.layout.summary_main_attack_group);
                setField(groupGroup, R.id.attack, grouped.get(attack), HIT, i, attack.getWeaponReference());
                setField(groupGroup, R.id.damage, attack.getWeapon().getDamage().toString(), DAMAGE, i, attack.getWeaponReference());
                setField(groupGroup, R.id.critical, attack.getWeapon().getCritical().toString(), CRIT_MULT, i, attack.getWeaponReference());
            }
        }

    }

    @Override
    protected void onSetFont() {
        super.onSetFont();
        setFontToLabels();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.summary_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                CharBase base = data.getBase();

                //edit opened character
                Intent intent = new Intent(activity, EditCharActivity.class);
                intent.putExtra(EXTRA_EDITED, base);
                int page = base.getRace() == null || base.getClassLevels().isEmpty() || base.getExperienceLevel() == 0 ?
                        EditCharPagerAdapter.PAGE_BASIC : EditCharPagerAdapter.PAGE_EQUIP;
                intent.putExtra(EXTRA_PAGE, page);
                activity.startActivityForResult(intent, REQUEST_EDIT);

                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void setFontToLabels(){
        final Typeface titleFont = new FontUtil(activity).getFont(BOLDER_FONT);
        final Typeface descFont = new FontUtil(activity).getFont(BOLD_FONT);
        RecursiveViewCaller<TextView> fontChange = new RecursiveViewCaller<TextView>(TextView.class){
            @Override
            protected void leafCall(TextView v, Object... params) {
                if (v.getId() == R.id.title) {
                    v.setTypeface(titleFont);
                } else if (v.getId() == R.id.description) {
                    v.setTypeface(descFont);
                }
            }
        };

        View rootView = findView(R.id.basic_root);
        fontChange.recursiveCall(rootView);
    }

    //TODO move to PageFragment
    private <T extends View> T findView(int... ids) {
        return ActivityUtil.findView(root, ids);
    }

}
