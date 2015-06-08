package com.vitobasso.d20charsheet.dao.abstraction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitobasso.d20charsheet.model.rulebook.Book;
import com.vitobasso.d20charsheet.model.rulebook.Rule;
import com.vitobasso.d20charsheet.util.app.AppGlobals;

import java.util.Collection;

import static com.vitobasso.d20charsheet.util.database.SQLiteHelper.COLUMN_NAME;
import static java.lang.String.format;

/**
 * Created by Victor on 26/04/2015.
 */
public abstract class AbstractRuleDao<T extends Rule> extends AbstractEntityDao<T> {

    public static final String COLUMN_BOOK_ID = "rulebook_id";

    private boolean ignoreActiveBooks;

    public AbstractRuleDao(Context context) {
        super(context);
    }

    protected AbstractRuleDao(Context context, SQLiteDatabase database) {
        super(context, database);
    }

    @Override
    public Cursor cursor(String selection) {
        String bookIds = getRulebookIdsAsString();
        if (!ignoreActiveBooks && bookIds != null) {
            String bookSelection = format("%s in (%s)", COLUMN_BOOK_ID, bookIds);
            selection = appendWhereClause(bookSelection, selection);
        }
        return super.cursor(selection);
    }

    @Override
    protected String orderBy() {
        return COLUMN_BOOK_ID + ", " + COLUMN_NAME;
    }

    @Override
    protected ContentValues toContentValues(T entity) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_ID, entity.getBook().getId());
        values.put(COLUMN_NAME, entity.getName());
        return values;
    }

    protected void setRulebook(Rule rule, Cursor cursor) {
        Book book = new Book();
        book.setId(getInt(cursor, COLUMN_BOOK_ID));
        rule.setBook(book);
    }

    private String getRulebookIdsAsString() {
        AppGlobals cache = (AppGlobals) context.getApplicationContext();
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

    public void setIgnoreBookSelection(boolean ignoreActiveBooks) {
        this.ignoreActiveBooks = ignoreActiveBooks;
    }

}
