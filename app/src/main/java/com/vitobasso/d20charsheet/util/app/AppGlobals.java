package com.vitobasso.d20charsheet.util.app;

import android.app.Application;
import android.util.Log;

import com.vitobasso.d20charsheet.dao.dependant.CharBookDao;
import com.vitobasso.d20charsheet.dao.entity.BookDao;
import com.vitobasso.d20charsheet.model.character.CharBase;
import com.vitobasso.d20charsheet.model.rulebook.Book;

import java.util.Collection;
import java.util.List;

/**
 * Created by Victor on 03/05/2015.
 */
public class AppGlobals extends Application {

    public static final String TAG = AppGlobals.class.getSimpleName();

    private CharBase openedChar;
    private Collection<Book> activeRulebooks;

    public void setOpenedChar(CharBase openedChar) {
        this.openedChar = openedChar;
        this.activeRulebooks = getCharBooks(openedChar);
    }

    private List<Book> getCharBooks(CharBase openedChar) {
        List<Book> activeBooks = null;
        if (openedChar != null) {
            CharBookDao charBookDao = new CharBookDao(this);
            activeBooks = charBookDao.findByParent(openedChar.getId());
            charBookDao.close();
        }
        return activeBooks;
    }

    public CharBase getOpenedChar() {
        return openedChar;
    }

    public boolean isCharOpened() {
        return openedChar != null;
    }

    public void setActiveRulebooks(Collection<Book> activeRulebooks) {
        this.activeRulebooks = activeRulebooks;
    }

    public Collection<Book> getActiveRulebooks() {
        if (activeRulebooks == null) {
            reset();
        }
        return activeRulebooks;
    }

    public void reset() {
        Log.i(TAG, "Reseting memory cache.");
        setOpenedChar(null);
        Collection<Book> defaultBooks = BookDao.getDefaultActiveBooks(this);
        if (defaultBooks == null) {
            throw new IllegalStateException("Can't find default active rulebooks");
        }
        setActiveRulebooks(defaultBooks);
    }

}
