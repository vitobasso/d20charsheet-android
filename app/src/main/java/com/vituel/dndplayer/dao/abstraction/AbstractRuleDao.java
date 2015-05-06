package com.vituel.dndplayer.dao.abstraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vituel.dndplayer.MemoryCache;
import com.vituel.dndplayer.model.rulebook.Book;
import com.vituel.dndplayer.model.rulebook.Rule;

import java.util.Collection;

import static com.vituel.dndplayer.util.database.SQLiteHelper.COLUMN_NAME;

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
        String bookIds = getRulebookIdsAsString();
        if (bookIds != null) {
            String selection = String.format("%s in (%s)", COLUMN_BOOK_ID, bookIds);
            return database.query(tableName(), allColumns(), selection, null, null, null, orderBy());
        } else {
            return super.listAllCursor();
        }
    }

    @Override
    protected String orderBy() {
        return COLUMN_BOOK_ID;
    }

    @Override
    protected ContentValues toContentValues(T entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_ID, entity.getBook().getId());
        values.put(COLUMN_NAME, entity.getName());
        return values;
    }

    protected void setRulebook(Rule rule, Cursor cursor, int col) {
        Book book = new Book();
        book.setId(cursor.getInt(col));
        rule.setBook(book);
    }

    private String getRulebookIdsAsString() {
        MemoryCache cache = (MemoryCache) context.getApplicationContext();
        Collection<Book> rulebooks = cache.getActiveRulebooks();
        if (rulebooks == null) {
            return null;
        }

        StringBuilder str = new StringBuilder();
        for (Book rulebook : rulebooks) {
            if (str.length() > 0) {
                str.append(",");
            }
            str.append(rulebook.getId());
        }

        return str.toString();
    }

}
