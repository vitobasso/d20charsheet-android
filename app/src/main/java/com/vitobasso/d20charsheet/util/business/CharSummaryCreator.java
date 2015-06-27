package com.vitobasso.d20charsheet.util.business;

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.vitobasso.d20charsheet.model.ClassLevel;
import com.vitobasso.d20charsheet.model.character.Attack;
import com.vitobasso.d20charsheet.model.character.AttackRound;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharSkill;
import com.vitobasso.d20charsheet.model.character.CharSummary;
import com.vitobasso.d20charsheet.model.effect.Condition;
import com.vitobasso.d20charsheet.model.item.WeaponProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.vitobasso.d20charsheet.model.Size.MEDIUM;

/**
 * Created by Victor on 13/06/2015.
 */
public class CharSummaryCreator {

    private Context context;

    private CharBase base;
    private CharSummary summary;

    public CharSummaryCreator(Context context) {
        this.context = context;
    }

    public CharSummary createFrom(CharBase base) {
        this.base = base;
        this.summary = new CharSummary();
        summary.setBase(base);

        initBasics();
        initAttacks();

        applyEffectsAndModifiers(base);
        applyTempHp(base);

        return summary;
    }

    private void applyTempHp(CharBase base) {
        int totalHp = summary.getHitPoints() + base.getDamageTaken().getTempHp();
        summary.setHitPoints(totalHp); //breakdown is managed in DamageGuiManager, separately from other effects
    }

    private void initBasics() {
        summary.setAttacks(new ArrayList<AttackRound>());
        summary.setSize(MEDIUM);
        summary.setSkills(new HashMap<String, CharSkill>());
        summary.setMaxDexMod(Integer.MAX_VALUE);
        summary.setReferencedConditions(new HashSet<Condition>());
    }

    /**
     * Uses the same model classes (Attack, WeaponProperties) as CharBase but clones them to
     * apply modifiers independently.
     */
    private void initAttacks() {
        Kryo cloner = new Kryo();
        summary.setAttacks(cloner.copy(base.getAttacks())); //cloned objects will be modified by magic bonuses, etc
        for (AttackRound attackRound : summary.getAttacks()) {
            initAttackRound(cloner, attackRound);
        }
    }

    private void initAttackRound(Kryo cloner, AttackRound attackRound) {
        for (Attack attack : attackRound.getAttacks()) {
            WeaponProperties weaponBase = base.getWeapon(attack.getWeaponReference());
            attack.setWeapon(cloner.copy(weaponBase));
        }
        if (isNullOrEmpty(attackRound.getName())) {
            AttackHelper attackHelper = new AttackHelper(context, base);
            String defaultName = attackHelper.createDefaultAttackRoundName(attackRound);
            attackRound.setName(defaultName);
        }
    }

    private void applyEffectsAndModifiers(CharBase base) {
        EffectApplier effectApplier = new EffectApplier(context, summary);
        ModifierCreator modifierCreator = new ModifierCreator(context, summary);

        //effects from race, class, feats, equipment and effects
        effectApplier.applyModifiers(modifierCreator.createBaseModifiers());
        if (base.getRace() != null) {
            effectApplier.apply(base.getRace());
            effectApplier.apply(base.getRace().getTraits());
        }
        for (ClassLevel classLevel : base.getClassLevels()) {
            effectApplier.apply(classLevel);
            effectApplier.apply(classLevel.getTraits());
        }
        effectApplier.apply(base.getFeats());
        effectApplier.apply(base.getEquipmentItems());
        effectApplier.apply(base.getActiveTempEffects());

        //effects from other attributes
        effectApplier.applyModifiers(modifierCreator.createAbilityModifiers().keySet());
        effectApplier.applyModifiers(modifierCreator.createSizeModifiers());
    }

}
