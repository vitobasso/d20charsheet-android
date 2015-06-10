package com.vitobasso.d20charsheet.model.rulebook;

import com.vitobasso.d20charsheet.model.AbstractEntity;

import org.codehaus.jackson.annotate.JsonValue;

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

    @JsonValue
    @Override
    public long getId() {
        return id;
    }

}
