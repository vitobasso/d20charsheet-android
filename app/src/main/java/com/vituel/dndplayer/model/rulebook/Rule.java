package com.vituel.dndplayer.model.rulebook;

import com.vituel.dndplayer.model.AbstractEntity;

/**
 * Created by Victor on 26/04/2015.
 */
public abstract class Rule extends AbstractEntity{

    private Book book;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
