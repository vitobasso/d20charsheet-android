package com.vitobasso.d20charsheet.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vitobasso.d20charsheet.dao.ActiveConditionDao;
import com.vitobasso.d20charsheet.dao.AttackDao;
import com.vitobasso.d20charsheet.dao.AttackRoundDao;
import com.vitobasso.d20charsheet.dao.abstraction.AbilityModifierDao;
import com.vitobasso.d20charsheet.dao.dependant.CharBookDao;
import com.vitobasso.d20charsheet.dao.dependant.CharClassDao;
import com.vitobasso.d20charsheet.dao.dependant.CharFeatDao;
import com.vitobasso.d20charsheet.dao.dependant.CharSkillDao;
import com.vitobasso.d20charsheet.dao.dependant.CharTempEffectDao;
import com.vitobasso.d20charsheet.dao.dependant.ClassTraitDao;
import com.vitobasso.d20charsheet.dao.dependant.ModifierDao;
import com.vitobasso.d20charsheet.dao.dependant.RaceTraitDao;
import com.vitobasso.d20charsheet.dao.dependant.WeaponDao;
import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.dao.entity.CharDao;
import com.vitobasso.d20charsheet.dao.entity.ClassDao;
import com.vitobasso.d20charsheet.dao.entity.ConditionDao;
import com.vitobasso.d20charsheet.dao.entity.EditionDao;
import com.vitobasso.d20charsheet.dao.entity.EffectDao;
import com.vitobasso.d20charsheet.dao.entity.FeatDao;
import com.vitobasso.d20charsheet.dao.entity.ItemDao;
import com.vitobasso.d20charsheet.dao.entity.RaceDao;
import com.vitobasso.d20charsheet.dao.entity.SkillDao;
import com.vitobasso.d20charsheet.dao.entity.TempEffectDao;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TAG = SQLiteHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "d20charsheet.db";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EFFECT_ID = "effect_id";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createAllTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String msg = String.format("Upgrading database from version %s to %s, which will destroy all old data.", oldVersion, newVersion);
        Log.w(TAG, msg);

        dropAllTables(db);
        createAllTables(db);
    }

    private void createAllTables(SQLiteDatabase database) {
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
        database.execSQL(CharBookDao.TABLE.toSql());
        database.execSQL(CharClassDao.TABLE.toSql());
        database.execSQL(CharFeatDao.TABLE.toSql());
        database.execSQL(CharTempEffectDao.TABLE.toSql());
        database.execSQL(CharSkillDao.TABLE.toSql());
        database.execSQL(ActiveConditionDao.TABLE.toSql());
        database.execSQL(AttackRoundDao.TABLE.toSql());
        database.execSQL(AttackDao.TABLE.toSql());
        database.execSQL(AbilityModifierDao.TABLE.toSql());
    }

    private void dropAllTables(SQLiteDatabase db) {
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
    }

}
