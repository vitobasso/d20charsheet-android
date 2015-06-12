package com.vitobasso.d20charsheet.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vitobasso.d20charsheet.dao.ActiveConditionDao;
import com.vitobasso.d20charsheet.dao.AttackRoundDao;
import com.vitobasso.d20charsheet.dao.abstraction.AbilityModifierDao;
import com.vitobasso.d20charsheet.dao.abstraction.AbstractEntityDao;
import com.vitobasso.d20charsheet.dao.dependant.CharClassDao;
import com.vitobasso.d20charsheet.dao.dependant.CharFeatDao;
import com.vitobasso.d20charsheet.dao.dependant.CharSkillDao;
import com.vitobasso.d20charsheet.dao.dependant.CharTempEffectDao;
import com.vitobasso.d20charsheet.model.AbstractEntity;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.character.CharEquip;
import com.vitobasso.d20charsheet.model.character.CharTempEffect;
import com.vitobasso.d20charsheet.model.character.DamageTaken;
import com.vitobasso.d20charsheet.util.database.Table;

import java.util.HashSet;
import java.util.List;

import static com.vitobasso.d20charsheet.util.database.ColumnType.INTEGER;
import static com.vitobasso.d20charsheet.util.database.ColumnType.REAL;
import static com.vitobasso.d20charsheet.util.database.ColumnType.TEXT;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_ID;
import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;

public class CharDao extends AbstractEntityDao<CharBase> {

    private static final String
            COLUMN_RACE_ID = "race_id",
            COLUMN_TENDENCY_MORAL = "tendency_moral",
            COLUMN_TENDENCY_LOYALTY = "tendency_loyalty",
            COLUMN_DIVINITY = "divinity",
            COLUMN_GENDER = "gender",
            COLUMN_AGE = "age",
            COLUMN_HEIGHT = "height",
            COLUMN_WEIGHT = "weight",
            COLUMN_PLAYER = "player",
            COLUMN_GM = "gm_name",
            COLUMN_CAMPAIGN = "campaign",
            COLUMN_CREATION_DATE = "creation_date",
            COLUMN_BASE_HP = "base_hp",
            COLUMN_BASE_STR = "base_str",
            COLUMN_BASE_DEX = "base_dex",
            COLUMN_BASE_CON = "base_con",
            COLUMN_BASE_INT = "base_int",
            COLUMN_BASE_WIS = "base_wis",
            COLUMN_BASE_CHA = "base_cha",
            COLUMN_DAMAGE_HP_LETHAL = "damage_hp_lethal",
            COLUMN_DAMAGE_HP_NONLETHAL = "damage_hp_nonlethal",
            COLUMN_TEMP_HP = "temp_hp",
            COLUMN_DAMAGE_STR = "damage_str",
            COLUMN_DAMAGE_DEX = "damage_dex",
            COLUMN_DAMAGE_CON = "damage_con",
            COLUMN_DAMAGE_INT = "damage_int",
            COLUMN_DAMAGE_WIS = "damage_wis",
            COLUMN_DAMAGE_CHA = "damage_cha",
            COLUMN_DAMAGE_LEVEL = "damage_level",
            COLUMN_EQUIP_MAINHAND = "equip_mainhand",
            COLUMN_EQUIP_OFFHAND = "equip_offhand",
            COLUMN_EQUIP_BODY = "equip_body",
            COLUMN_EQUIP_HEAD = "equip_head",
            COLUMN_EQUIP_EYES = "equip_eyes",
            COLUMN_EQUIP_NECK = "equip_neck",
            COLUMN_EQUIP_TORSO = "equip_torso",
            COLUMN_EQUIP_WAIST = "equip_waist",
            COLUMN_EQUIP_SHOULDERS = "equip_shoulders",
            COLUMN_EQUIP_ARMS = "equip_arms",
            COLUMN_EQUIP_HANDS = "equip_hands",
            COLUMN_EQUIP_FINGER1 = "equip_finger1",
            COLUMN_EQUIP_FINGER2 = "equip_finger2",
            COLUMN_EQUIP_FEET = "equip_feet";

    public static final Table TABLE = new Table("character")
            .colNotNull(COLUMN_NAME, TEXT)
            .col(COLUMN_RACE_ID, INTEGER)
            .col(COLUMN_TENDENCY_MORAL, TEXT)
            .col(COLUMN_TENDENCY_LOYALTY, TEXT)
            .col(COLUMN_DIVINITY, TEXT)
            .col(COLUMN_GENDER, TEXT)
            .col(COLUMN_AGE, INTEGER)
            .col(COLUMN_HEIGHT, REAL)
            .col(COLUMN_WEIGHT, REAL)
            .col(COLUMN_PLAYER, TEXT)
            .col(COLUMN_GM, TEXT)
            .col(COLUMN_CAMPAIGN, TEXT)
            .col(COLUMN_CREATION_DATE, TEXT)
            .colNotNull(COLUMN_BASE_HP, INTEGER)
            .colNotNull(COLUMN_BASE_STR, INTEGER)
            .colNotNull(COLUMN_BASE_DEX, INTEGER)
            .colNotNull(COLUMN_BASE_CON, INTEGER)
            .colNotNull(COLUMN_BASE_INT, INTEGER)
            .colNotNull(COLUMN_BASE_WIS, INTEGER)
            .colNotNull(COLUMN_BASE_CHA, INTEGER)
            .colNotNull(COLUMN_DAMAGE_HP_LETHAL, INTEGER)
            .colNotNull(COLUMN_DAMAGE_HP_NONLETHAL, INTEGER)
            .colNotNull(COLUMN_TEMP_HP, INTEGER)
            .colNotNull(COLUMN_DAMAGE_STR, INTEGER)
            .colNotNull(COLUMN_DAMAGE_DEX, INTEGER)
            .colNotNull(COLUMN_DAMAGE_CON, INTEGER)
            .colNotNull(COLUMN_DAMAGE_INT, INTEGER)
            .colNotNull(COLUMN_DAMAGE_WIS, INTEGER)
            .colNotNull(COLUMN_DAMAGE_CHA, INTEGER)
            .colNotNull(COLUMN_DAMAGE_LEVEL, INTEGER)
            .col(COLUMN_EQUIP_MAINHAND, INTEGER)
            .col(COLUMN_EQUIP_OFFHAND, INTEGER)
            .col(COLUMN_EQUIP_BODY, INTEGER)
            .col(COLUMN_EQUIP_HEAD, INTEGER)
            .col(COLUMN_EQUIP_EYES, INTEGER)
            .col(COLUMN_EQUIP_NECK, INTEGER)
            .col(COLUMN_EQUIP_TORSO, INTEGER)
            .col(COLUMN_EQUIP_WAIST, INTEGER)
            .col(COLUMN_EQUIP_SHOULDERS, INTEGER)
            .col(COLUMN_EQUIP_ARMS, INTEGER)
            .col(COLUMN_EQUIP_HANDS, INTEGER)
            .col(COLUMN_EQUIP_FINGER1, INTEGER)
            .col(COLUMN_EQUIP_FINGER2, INTEGER)
            .col(COLUMN_EQUIP_FEET, INTEGER);

    public CharDao(Context context) {
        super(context);
    }

    @Override
    protected Table getTable() {
        return TABLE;
    }

    @Override
    protected ContentValues toContentValues(CharBase entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entity.getName());
        values.put(COLUMN_TENDENCY_MORAL, entity.getTendencyMoral());
        values.put(COLUMN_TENDENCY_LOYALTY, entity.getTendencyLoyality());
        values.put(COLUMN_DIVINITY, entity.getDivinity());
        values.put(COLUMN_GENDER, entity.getGender());
        values.put(COLUMN_AGE, entity.getAge());
        values.put(COLUMN_HEIGHT, entity.getHeight());
        values.put(COLUMN_WEIGHT, entity.getWeight());
        values.put(COLUMN_PLAYER, entity.getPlayer());
        values.put(COLUMN_GM, entity.getDungeonMaster());
        values.put(COLUMN_CAMPAIGN, entity.getCampaign());
        values.put(COLUMN_CREATION_DATE, entity.getCreationDate());
        values.put(COLUMN_BASE_HP, entity.getHitPoints());
        values.put(COLUMN_BASE_STR, entity.getStrength());
        values.put(COLUMN_BASE_DEX, entity.getDexterity());
        values.put(COLUMN_BASE_CON, entity.getConstitution());
        values.put(COLUMN_BASE_INT, entity.getIntelligence());
        values.put(COLUMN_BASE_WIS, entity.getWisdom());
        values.put(COLUMN_BASE_CHA, entity.getCharisma());

        DamageTaken damage = entity.getDamageTaken();
        values.put(COLUMN_DAMAGE_HP_LETHAL, damage.getLethal());
        values.put(COLUMN_DAMAGE_HP_NONLETHAL, damage.getNonlethal());
        values.put(COLUMN_TEMP_HP, damage.getTempHp());
        values.put(COLUMN_DAMAGE_STR, damage.getStrength());
        values.put(COLUMN_DAMAGE_DEX, damage.getDexterity());
        values.put(COLUMN_DAMAGE_CON, damage.getConstitution());
        values.put(COLUMN_DAMAGE_INT, damage.getIntelligence());
        values.put(COLUMN_DAMAGE_WIS, damage.getWisdom());
        values.put(COLUMN_DAMAGE_CHA, damage.getCharisma());
        values.put(COLUMN_DAMAGE_LEVEL, damage.getLevel());

        CharEquip equip = entity.getEquipment();
        putEntityId(values, COLUMN_EQUIP_MAINHAND, equip.getMainHand().getItem());
        putEntityId(values, COLUMN_EQUIP_OFFHAND, equip.getOffhand().getItem());
        putEntityId(values, COLUMN_EQUIP_BODY, equip.getBody().getItem());
        putEntityId(values, COLUMN_EQUIP_HEAD, equip.getHead().getItem());
        putEntityId(values, COLUMN_EQUIP_EYES, equip.getEyes().getItem());
        putEntityId(values, COLUMN_EQUIP_NECK, equip.getNeck().getItem());
        putEntityId(values, COLUMN_EQUIP_TORSO, equip.getTorso().getItem());
        putEntityId(values, COLUMN_EQUIP_WAIST, equip.getWaist().getItem());
        putEntityId(values, COLUMN_EQUIP_SHOULDERS, equip.getShoulders().getItem());
        putEntityId(values, COLUMN_EQUIP_ARMS, equip.getArms().getItem());
        putEntityId(values, COLUMN_EQUIP_HANDS, equip.getHands().getItem());
        putEntityId(values, COLUMN_EQUIP_FINGER1, equip.getFinger1().getItem());
        putEntityId(values, COLUMN_EQUIP_FINGER2, equip.getFinger2().getItem());
        putEntityId(values, COLUMN_EQUIP_FEET, equip.getFeet().getItem());

        //race
        if (entity.getRace() != null) {
            values.put(COLUMN_RACE_ID, entity.getRace().getId());
        }

        return values;
    }

    private void putEntityId(ContentValues values, String key, AbstractEntity entity){
        if(entity != null) {
            values.put(key, entity.getId());
        }
    }

    @Override
    public void postSave(CharBase entity) {
        long id = entity.getId();

        //classes
        CharClassDao classDao = new CharClassDao(context, database);
        classDao.saveOverwrite(id, entity.getClassLevels());

        //feats
        CharFeatDao charFeatDao = new CharFeatDao(context, database);
        charFeatDao.saveOverwrite(id, entity.getFeats());

        //skills
        CharSkillDao charSkillDao = new CharSkillDao(context, database);
        charSkillDao.saveOverwrite(id, entity.getSkills());

        //attacks
        AttackRoundDao attackRoundDao = new AttackRoundDao(context, database);
        attackRoundDao.removeAllForChar(id);
        attackRoundDao.save(entity.getAttacks(), id);

        //ability modifiers
        AbilityModifierDao abilityModDao = new AbilityModifierDao(context, database);
        abilityModDao.saveOverwrite(id, entity.getAbilityMods());

        //temp effects are saved directly on TempEffectActivityDao from SummaryTempEffectsFragment
        //active conditions are saved directly on ActiveConditionDao from SummaryActivity
    }

    @Override
    public CharBase fromCursor(Cursor cursor) {

        CharBase c = new CharBase();
        c.setId(getLong(cursor, COLUMN_ID));
        c.setName(getString(cursor, COLUMN_NAME));
        c.setTendencyMoral(getString(cursor, COLUMN_TENDENCY_MORAL));
        c.setTendencyLoyality(getString(cursor, COLUMN_TENDENCY_LOYALTY));
        c.setDivinity(getString(cursor, COLUMN_DIVINITY));
        c.setGender(getString(cursor, COLUMN_GENDER));
        c.setAge(getInt(cursor, COLUMN_AGE));
        c.setHeight(getDouble(cursor, COLUMN_HEIGHT));
        c.setWeight(getDouble(cursor, COLUMN_WEIGHT));
        c.setPlayer(getString(cursor, COLUMN_PLAYER));
        c.setDungeonMaster(getString(cursor, COLUMN_GM));
        c.setCampaign(getString(cursor, COLUMN_CAMPAIGN));
        c.setCreationDate(getString(cursor, COLUMN_CREATION_DATE));
        c.setHitPoints(getInt(cursor, COLUMN_BASE_HP));
        c.setStrength(getInt(cursor, COLUMN_BASE_STR));
        c.setDexterity(getInt(cursor, COLUMN_BASE_DEX));
        c.setConstitution(getInt(cursor, COLUMN_BASE_CON));
        c.setIntelligence(getInt(cursor, COLUMN_BASE_INT));
        c.setWisdom(getInt(cursor, COLUMN_BASE_WIS));
        c.setCharisma(getInt(cursor, COLUMN_BASE_CHA));

        //damage
        DamageTaken dmg = new DamageTaken();
        dmg.setLethal(getInt(cursor, COLUMN_DAMAGE_LEVEL));
        dmg.setNonlethal(getInt(cursor, COLUMN_DAMAGE_HP_NONLETHAL));
        dmg.setTempHp(getInt(cursor, COLUMN_TEMP_HP));
        dmg.setStrength(getInt(cursor, COLUMN_DAMAGE_STR));
        dmg.setDexterity(getInt(cursor, COLUMN_DAMAGE_DEX));
        dmg.setConstitution(getInt(cursor, COLUMN_DAMAGE_CON));
        dmg.setIntelligence(getInt(cursor, COLUMN_DAMAGE_INT));
        dmg.setWisdom(getInt(cursor, COLUMN_DAMAGE_WIS));
        dmg.setCharisma(getInt(cursor, COLUMN_DAMAGE_CHA));
        dmg.setLevel(getInt(cursor, COLUMN_DAMAGE_LEVEL));
        c.setDamageTaken(dmg);

        //equip
        ItemDao itemDao = new ItemDao(context, database);
        itemDao.setIgnoreBookSelection(true);
        c.setEquipment(new CharEquip());
        c.getEquipment().getMainHand().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_MAINHAND)));
        c.getEquipment().getOffhand().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_OFFHAND)));
        c.getEquipment().getBody().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_BODY)));
        c.getEquipment().getHead().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_HEAD)));
        c.getEquipment().getEyes().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_EYES)));
        c.getEquipment().getNeck().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_NECK)));
        c.getEquipment().getTorso().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_TORSO)));
        c.getEquipment().getWaist().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_WAIST)));
        c.getEquipment().getShoulders().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_SHOULDERS)));
        c.getEquipment().getArms().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_ARMS)));
        c.getEquipment().getHands().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_HANDS)));
        c.getEquipment().getFinger1().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_FINGER1)));
        c.getEquipment().getFinger2().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_FINGER2)));
        c.getEquipment().getFeet().setItem(itemDao.findById(getInt(cursor, COLUMN_EQUIP_FEET)));

        //ability modifiers
        AbilityModifierDao abilityModDao = new AbilityModifierDao(context, database);
        c.setAbilityMods(abilityModDao.findByParent(c.getId()));

        //attacks
        AttackRoundDao attacksDao = new AttackRoundDao(context, database);
        c.setAttacks(attacksDao.listForChar(c.getId()));

        //load race
        RaceDao raceDao = new RaceDao(context, database);
        raceDao.setIgnoreBookSelection(true);
        c.setRace(raceDao.findById(cursor.getInt(2)));

        //load class
        CharClassDao classDao = new CharClassDao(context, database);
        classDao.setIgnoreBookSelection(true);
        c.setClassLevels(classDao.findByParent(c.getId()));

        //load feats
        CharFeatDao charFeatDao = new CharFeatDao(context, database);
        charFeatDao.setIgnoreBookSelection(true);
        c.setFeats(charFeatDao.findByParent(c.getId()));

        //load skills
        CharSkillDao charSkillDao = new CharSkillDao(context, database);
        c.setSkills(charSkillDao.findByParent(c.getId()));

        //load temporary effects
        CharTempEffectDao activeTempDao = new CharTempEffectDao(context, database);
        List<CharTempEffect> list = activeTempDao.findByParent(c.getId());
        for (CharTempEffect acond : list) {
            c.getTempEffects().add(acond);
        }

        //load active conditions
        ActiveConditionDao condDao = new ActiveConditionDao(context, database);
        c.setActiveConditions(new HashSet<>(condDao.findByParent(c.getId())));

        return c;
    }

    @Override
    public CharBase fromCursorBrief(Cursor cursor) {

        CharBase c = new CharBase();
        c.setId(getLong(cursor, COLUMN_ID));
        c.setName(getString(cursor, COLUMN_NAME));

        //load race
        RaceDao raceDao = new RaceDao(context, database);
        raceDao.setIgnoreBookSelection(true);
        c.setRace(raceDao.findById(cursor.getInt(2))); //TODO findByIdBrief

        //load class
        CharClassDao classDao = new CharClassDao(context, database);
        classDao.setIgnoreBookSelection(true);
        c.setClassLevels(classDao.findByParent(c.getId())); //TODO findByIdBrief

        return c;
    }
}
