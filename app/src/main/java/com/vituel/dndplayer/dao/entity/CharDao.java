package com.vituel.dndplayer.dao.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vituel.dndplayer.dao.ActiveConditionDao;
import com.vituel.dndplayer.dao.AttackRoundDao;
import com.vituel.dndplayer.dao.abstraction.AbilityModifierDao;
import com.vituel.dndplayer.dao.abstraction.AbstractEntityDao;
import com.vituel.dndplayer.dao.dependant.CharClassDao;
import com.vituel.dndplayer.dao.dependant.CharFeatDao;
import com.vituel.dndplayer.dao.dependant.CharSkillDao;
import com.vituel.dndplayer.dao.dependant.CharTempEffectDao;
import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.character.CharEquip;
import com.vituel.dndplayer.model.character.CharTempEffect;
import com.vituel.dndplayer.model.character.DamageTaken;

import java.util.HashSet;
import java.util.List;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_ID;
import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

public class CharDao extends AbstractEntityDao<CharBase> {

    public static final String TABLE = "character";

    private static final String COLUMN_RACE_ID = "race_id";
    private static final String COLUMN_TENDENCY_MORAL = "tendency_moral";
    private static final String COLUMN_TENDENCY_LOYALTY = "tendency_loyalty";
    private static final String COLUMN_DIVINITY = "divinity";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_HEIGHT = "height";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_PLAYER = "player";
    private static final String COLUMN_GM = "gm_name";
    private static final String COLUMN_CAMPAIGN = "campaign";
    private static final String COLUMN_CREATION_DATE = "creation_date";
    private static final String COLUMN_BASE_HP = "base_hp";
    private static final String COLUMN_BASE_STR = "base_str";
    private static final String COLUMN_BASE_DEX = "base_dex";
    private static final String COLUMN_BASE_CON = "base_con";
    private static final String COLUMN_BASE_INT = "base_int";
    private static final String COLUMN_BASE_WIS = "base_wis";
    private static final String COLUMN_BASE_CHA = "base_cha";
    private static final String COLUMN_DAMAGE_HP_LETHAL = "damage_hp_lethal";
    private static final String COLUMN_DAMAGE_HP_NONLETHAL = "damage_hp_nonlethal";
    private static final String COLUMN_TEMP_HP = "temp_hp";
    private static final String COLUMN_DAMAGE_STR = "damage_str";
    private static final String COLUMN_DAMAGE_DEX = "damage_dex";
    private static final String COLUMN_DAMAGE_CON = "damage_con";
    private static final String COLUMN_DAMAGE_INT = "damage_int";
    private static final String COLUMN_DAMAGE_WIS = "damage_wis";
    private static final String COLUMN_DAMAGE_CHA = "damage_cha";
    private static final String COLUMN_DAMAGE_LEVEL = "damage_level";
    private static final String COLUMN_EQUIP_MAINHAND = "equip_mainhand";
    private static final String COLUMN_EQUIP_OFFHAND = "equip_offhand";
    private static final String COLUMN_EQUIP_BODY = "equip_body";
    private static final String COLUMN_EQUIP_HEAD = "equip_head";
    private static final String COLUMN_EQUIP_EYES = "equip_eyes";
    private static final String COLUMN_EQUIP_NECK = "equip_neck";
    private static final String COLUMN_EQUIP_TORSO = "equip_torso";
    private static final String COLUMN_EQUIP_WAIST = "equip_waist";
    private static final String COLUMN_EQUIP_SHOULDERS = "equip_shoulders";
    private static final String COLUMN_EQUIP_ARMS = "equip_arms";
    private static final String COLUMN_EQUIP_HANDS = "equip_hands";
    private static final String COLUMN_EQUIP_FINGER1 = "equip_finger1";
    private static final String COLUMN_EQUIP_FINGER2 = "equip_finger2";
    private static final String COLUMN_EQUIP_FEET = "equip_feet";


    public static final String CREATE_TABLE = "create table " + TABLE + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_RACE_ID + " integer, "
            + COLUMN_TENDENCY_MORAL + " text, "
            + COLUMN_TENDENCY_LOYALTY + " text, "
            + COLUMN_DIVINITY + " text, "
            + COLUMN_GENDER + " text, "
            + COLUMN_AGE + " integer, "
            + COLUMN_HEIGHT + " real, "
            + COLUMN_WEIGHT + " real, "
            + COLUMN_PLAYER + " text, "
            + COLUMN_GM + " text, "
            + COLUMN_CAMPAIGN + " text, "
            + COLUMN_CREATION_DATE + " text, "
            + COLUMN_BASE_HP + " integer not null, "
            + COLUMN_BASE_STR + " integer not null, "
            + COLUMN_BASE_DEX + " integer not null, "
            + COLUMN_BASE_CON + " integer not null, "
            + COLUMN_BASE_INT + " integer not null, "
            + COLUMN_BASE_WIS + " integer not null, "
            + COLUMN_BASE_CHA + " integer not null, "
            + COLUMN_DAMAGE_HP_LETHAL + " integer not null, "
            + COLUMN_DAMAGE_HP_NONLETHAL + " integer not null, "
            + COLUMN_TEMP_HP + " integer not null, "
            + COLUMN_DAMAGE_STR + " integer not null, "
            + COLUMN_DAMAGE_DEX + " integer not null, "
            + COLUMN_DAMAGE_CON + " integer not null, "
            + COLUMN_DAMAGE_INT + " integer not null, "
            + COLUMN_DAMAGE_WIS + " integer not null, "
            + COLUMN_DAMAGE_CHA + " integer not null, "
            + COLUMN_DAMAGE_LEVEL + " integer not null, "
            + COLUMN_EQUIP_MAINHAND + " integer, "
            + COLUMN_EQUIP_OFFHAND + " integer, "
            + COLUMN_EQUIP_BODY + " integer, "
            + COLUMN_EQUIP_HEAD + " integer, "
            + COLUMN_EQUIP_EYES + " integer, "
            + COLUMN_EQUIP_NECK + " integer, "
            + COLUMN_EQUIP_TORSO + " integer, "
            + COLUMN_EQUIP_WAIST + " integer, "
            + COLUMN_EQUIP_SHOULDERS + " integer, "
            + COLUMN_EQUIP_ARMS + " integer, "
            + COLUMN_EQUIP_HANDS + " integer, "
            + COLUMN_EQUIP_FINGER1 + " integer, "
            + COLUMN_EQUIP_FINGER2 + " integer, "
            + COLUMN_EQUIP_FEET + " integer"
            + ");";

    public CharDao(Context context) {
        super(context);
    }

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String[] allColumns() {
        return new String[]{
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_RACE_ID,
                COLUMN_TENDENCY_MORAL,
                COLUMN_TENDENCY_LOYALTY,
                COLUMN_DIVINITY,
                COLUMN_GENDER,
                COLUMN_AGE,
                COLUMN_HEIGHT,
                COLUMN_WEIGHT,
                COLUMN_PLAYER,
                COLUMN_GM,
                COLUMN_CAMPAIGN,
                COLUMN_CREATION_DATE,
                COLUMN_BASE_HP,
                COLUMN_BASE_STR,
                COLUMN_BASE_DEX,
                COLUMN_BASE_CON,
                COLUMN_BASE_INT,
                COLUMN_BASE_WIS,
                COLUMN_BASE_CHA,
                COLUMN_DAMAGE_HP_LETHAL,
                COLUMN_DAMAGE_HP_LETHAL,
                COLUMN_TEMP_HP,
                COLUMN_DAMAGE_STR,
                COLUMN_DAMAGE_DEX,
                COLUMN_DAMAGE_CON,
                COLUMN_DAMAGE_INT,
                COLUMN_DAMAGE_WIS,
                COLUMN_DAMAGE_CHA,
                COLUMN_DAMAGE_LEVEL,
                COLUMN_EQUIP_MAINHAND,
                COLUMN_EQUIP_OFFHAND,
                COLUMN_EQUIP_BODY,
                COLUMN_EQUIP_HEAD,
                COLUMN_EQUIP_EYES,
                COLUMN_EQUIP_NECK,
                COLUMN_EQUIP_TORSO,
                COLUMN_EQUIP_WAIST,
                COLUMN_EQUIP_SHOULDERS,
                COLUMN_EQUIP_ARMS,
                COLUMN_EQUIP_HANDS,
                COLUMN_EQUIP_FINGER1,
                COLUMN_EQUIP_FINGER2,
                COLUMN_EQUIP_FEET
        };
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
        c.setId(cursor.getLong(0));
        c.setName(cursor.getString(1));
        c.setTendencyMoral(cursor.getString(3));
        c.setTendencyLoyality(cursor.getString(4));
        c.setDivinity(cursor.getString(5));
        c.setGender(cursor.getString(6));
        c.setAge(cursor.getInt(7));
        c.setHeight(cursor.getDouble(8));
        c.setWeight(cursor.getDouble(9));
        c.setPlayer(cursor.getString(10));
        c.setDungeonMaster(cursor.getString(11));
        c.setCampaign(cursor.getString(12));
        c.setCreationDate(cursor.getString(13));
        c.setHitPoints(cursor.getInt(14));
        c.setStrength(cursor.getInt(15));
        c.setDexterity(cursor.getInt(16));
        c.setConstitution(cursor.getInt(17));
        c.setIntelligence(cursor.getInt(18));
        c.setWisdom(cursor.getInt(19));
        c.setCharisma(cursor.getInt(20));

        //damage
        DamageTaken dmg = new DamageTaken();
        dmg.setLethal(cursor.getInt(21));
        dmg.setNonlethal(cursor.getInt(22));
        dmg.setTempHp(cursor.getInt(23));
        dmg.setStrength(cursor.getInt(24));
        dmg.setDexterity(cursor.getInt(25));
        dmg.setConstitution(cursor.getInt(26));
        dmg.setIntelligence(cursor.getInt(27));
        dmg.setWisdom(cursor.getInt(28));
        dmg.setCharisma(cursor.getInt(29));
        dmg.setLevel(cursor.getInt(30));
        c.setDamageTaken(dmg);

        //equip
        ItemDao itemDao = new ItemDao(context, database);
        itemDao.setIgnoreBookSelection(true);
        c.setEquipment(new CharEquip());
        c.getEquipment().getMainHand().setItem(itemDao.findById(cursor.getInt(31)));
        c.getEquipment().getOffhand().setItem(itemDao.findById(cursor.getInt(32)));
        c.getEquipment().getBody().setItem(itemDao.findById(cursor.getInt(33)));
        c.getEquipment().getHead().setItem(itemDao.findById(cursor.getInt(34)));
        c.getEquipment().getEyes().setItem(itemDao.findById(cursor.getInt(35)));
        c.getEquipment().getNeck().setItem(itemDao.findById(cursor.getInt(36)));
        c.getEquipment().getTorso().setItem(itemDao.findById(cursor.getInt(37)));
        c.getEquipment().getWaist().setItem(itemDao.findById(cursor.getInt(38)));
        c.getEquipment().getShoulders().setItem(itemDao.findById(cursor.getInt(39)));
        c.getEquipment().getArms().setItem(itemDao.findById(cursor.getInt(40)));
        c.getEquipment().getHands().setItem(itemDao.findById(cursor.getInt(41)));
        c.getEquipment().getFinger1().setItem(itemDao.findById(cursor.getInt(42)));
        c.getEquipment().getFinger2().setItem(itemDao.findById(cursor.getInt(43)));
        c.getEquipment().getFeet().setItem(itemDao.findById(cursor.getInt(44)));

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
        c.setActiveConditions(new HashSet<>(condDao.findByChar(c.getId())));

        return c;
    }
}
