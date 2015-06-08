package com.vitobasso.d20charsheet.model.rulebook;

import com.vitobasso.d20charsheet.model.AbstractEntity;

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
