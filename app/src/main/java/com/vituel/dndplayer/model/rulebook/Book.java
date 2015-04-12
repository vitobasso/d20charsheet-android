package com.vituel.dndplayer.model.rulebook;

import com.vituel.dndplayer.model.AbstractEntity;

/**
 * Created by Victor on 12/04/2015.
 */
public class Book extends AbstractEntity {

    private Edition edition;
    private String abbreviation;
    private int year;

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
