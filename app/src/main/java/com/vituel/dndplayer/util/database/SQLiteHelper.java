package com.vituel.dndplayer.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vituel.dndplayer.dao.ActiveConditionDao;
import com.vituel.dndplayer.dao.AttackDao;
import com.vituel.dndplayer.dao.AttackRoundDao;
import com.vituel.dndplayer.dao.abstraction.AbilityModifierDao;
import com.vituel.dndplayer.dao.dependant.CharBookDao;
import com.vituel.dndplayer.dao.dependant.CharClassDao;
import com.vituel.dndplayer.dao.dependant.CharFeatDao;
import com.vituel.dndplayer.dao.dependant.CharSkillDao;
import com.vituel.dndplayer.dao.dependant.CharTempEffectDao;
import com.vituel.dndplayer.dao.dependant.ClassTraitDao;
import com.vituel.dndplayer.dao.dependant.ModifierDao;
import com.vituel.dndplayer.dao.dependant.RaceTraitDao;
import com.vituel.dndplayer.dao.dependant.WeaponDao;
import com.vituel.dndplayer.dao.entity.BookDao;
import com.vituel.dndplayer.dao.entity.CharDao;
import com.vituel.dndplayer.dao.entity.ClassDao;
import com.vituel.dndplayer.dao.entity.ConditionDao;
import com.vituel.dndplayer.dao.entity.EditionDao;
import com.vituel.dndplayer.dao.entity.EffectDao;
import com.vituel.dndplayer.dao.entity.FeatDao;
import com.vituel.dndplayer.dao.entity.ItemDao;
import com.vituel.dndplayer.dao.entity.RaceDao;
import com.vituel.dndplayer.dao.entity.SkillDao;
import com.vituel.dndplayer.dao.entity.TempEffectDao;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dndplayer.db";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EFFECT_ID = "effect_id";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        //library
        database.execSQL(EditionDao.TABLE.toSql());
        database.execSQL(BookDao.TABLE.toSql());
        database.execSQL(RaceDao.TABLE.toSql());
        database.execSQL(RaceTraitDao.TABLE.toSql());
        database.execSQL(ClassDao.TABLE.toSql());
        database.execSQL(ClassTraitDao.TABLE.toSql());
        database.execSQL(ItemDao.TABLE.toSql());
        database.execSQL(WeaponDao.TABLE.toSql());
        database.execSQL(FeatDao.TABLE.toSql());
        database.execSQL(TempEffectDao.TABLE.toSql());
        database.execSQL(EffectDao.TABLE.toSql());
        database.execSQL(ModifierDao.TABLE.toSql());
        database.execSQL(SkillDao.TABLE.toSql());
        database.execSQL(ConditionDao.TABLE.toSql());

        //char
        database.execSQL(CharDao.TABLE.toSql());
        database.execSQL(CharBookDao.CREATE_TABLE);
        database.execSQL(CharClassDao.CREATE_TABLE);
        database.execSQL(CharFeatDao.CREATE_TABLE);
        database.execSQL(CharTempEffectDao.CREATE_TABLE);
        database.execSQL(CharSkillDao.CREATE_TABLE);
        database.execSQL(ActiveConditionDao.CREATE_TABLE);
        database.execSQL(AttackRoundDao.CREATE_TABLE);
        database.execSQL(AttackDao.CREATE_TABLE);
        database.execSQL(AbilityModifierDao.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        //library
        db.execSQL("DROP TABLE IF EXISTS " + EditionDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BookDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RaceDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RaceTraitDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ClassDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ClassTraitDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ItemDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WeaponDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FeatDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TempEffectDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EffectDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ModifierDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SkillDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ConditionDao.TABLE);

        //char
        db.execSQL("DROP TABLE IF EXISTS " + CharDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharBookDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharClassDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharFeatDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharTempEffectDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharSkillDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveConditionDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AttackRoundDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AttackDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AbilityModifierDao.TABLE);
        onCreate(db);

    }

}
