package com.vituel.dndplayer.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.vituel.dndplayer.model.AbstractEntity;
import com.vituel.dndplayer.model.rulebook.Rule;
import com.vituel.dndplayer.util.database.SQLiteHelper;

import java.text.MessageFormat;

import static com.vituel.dndplayer.util.database.SQLiteHelper.*;

/**
 * Created by Victor on 26/04/2015.
 */
public abstract class AbstractRuleDao<T extends Rule> extends AbstractEntityDao<T> {

    public static final String COLUMN_BOOK_ID = "rulebook_id";

    public AbstractRuleDao(Context context) {
        super(context);
    }

    protected AbstractRuleDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Cursor listAllCursor() {
        String join = String.format("%s rule left join %s book on rule.%s = book.%s", tableName(), BookDao.TABLE, COLUMN_BOOK_ID, COLUMN_ID);
        return database.query(join, allColumns(), null, null, null, orderBy(), null);
    }

    @Override
    protected String orderBy() {
        return COLUMN_BOOK_ID;
    }
}
