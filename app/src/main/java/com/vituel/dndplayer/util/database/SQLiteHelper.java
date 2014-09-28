package com.vituel.dndplayer.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vituel.dndplayer.dao.ActiveConditionDao;
import com.vituel.dndplayer.dao.AttackDao;
import com.vituel.dndplayer.dao.AttackRoundDao;
import com.vituel.dndplayer.dao.CharClassDao;
import com.vituel.dndplayer.dao.CharDao;
import com.vituel.dndplayer.dao.CharSkillDao;
import com.vituel.dndplayer.dao.ClassDao;
import com.vituel.dndplayer.dao.ItemDao;
import com.vituel.dndplayer.dao.ModifierDao;
import com.vituel.dndplayer.dao.RaceDao;
import com.vituel.dndplayer.dao.SkillDao;
import com.vituel.dndplayer.dao.TempEffectActivityDao;
import com.vituel.dndplayer.dao.TempEffectDao;
import com.vituel.dndplayer.dao.TraitDao;
import com.vituel.dndplayer.dao.TraitLinkDao;
import com.vituel.dndplayer.dao.WeaponDao;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dndplayer.db";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CharDao.CREATE_TABLE);
        database.execSQL(RaceDao.CREATE_TABLE);
        database.execSQL(ClassDao.CREATE_TABLE);
        database.execSQL(CharClassDao.CREATE_TABLE);
        database.execSQL(ItemDao.CREATE_TABLE);
        database.execSQL(ModifierDao.CREATE_TABLE);
        database.execSQL(TempEffectDao.CREATE_TABLE);
        database.execSQL(TempEffectActivityDao.CREATE_TABLE);
        database.execSQL(TraitDao.CREATE_TABLE);
        database.execSQL(TraitLinkDao.CREATE_TABLE);
        database.execSQL(SkillDao.CREATE_TABLE);
        database.execSQL(CharSkillDao.CREATE_TABLE);
        database.execSQL(ActiveConditionDao.CREATE_TABLE);
        database.execSQL(AttackRoundDao.CREATE_TABLE);
        database.execSQL(AttackDao.CREATE_TABLE);
        database.execSQL(WeaponDao.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        //reset db
        db.execSQL("DROP TABLE IF EXISTS " + CharDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RaceDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ClassDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharClassDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ItemDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ModifierDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TempEffectDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TempEffectActivityDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TraitDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TraitLinkDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SkillDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CharSkillDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ActiveConditionDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AttackRoundDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AttackDao.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WeaponDao.TABLE);
        onCreate(db);

    }

}
