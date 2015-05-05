package com.vituel.dndplayer;

import android.app.Application;

import com.vituel.dndplayer.dao.CharBookDao;
import com.vituel.dndplayer.model.character.CharBase;
import com.vituel.dndplayer.model.rulebook.Book;

import java.util.Collection;

/**
 * Created by Victor on 03/05/2015.
 */
public class MemoryCache extends Application {

    private CharBase openedChar;
    private Collection<Book> activeRulebooks;

    public void setOpenedChar(CharBase openedChar) {
        this.openedChar = openedChar;

        CharBookDao charBookDao = new CharBookDao(this);
        this.activeRulebooks = charBookDao.findByParent(openedChar.getId());
    }

    public CharBase getOpenedChar() {
        return openedChar;
    }

    public void setActiveRulebooks(Collection<Book> activeRulebooks) {
        this.activeRulebooks = activeRulebooks;
    }

    public Collection<Book> getActiveRulebooks() {
        return activeRulebooks;
    }

}
