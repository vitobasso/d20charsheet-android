package com.vitobasso.d20charsheet.model.rulebook;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.vitobasso.d20charsheet.model.AbstractEntity;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Created by Victor on 12/04/2015.
 */
public class Book extends AbstractEntity {

    private Edition edition;
    private String abbreviation;
    private Integer year;

    public Book() {
    }

    //used by jackson
    public Book(int id) {
        this.id = id;
    }

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @JsonValue
    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    protected int subHashCode() {
        return Objects.hashCode(edition, name, year);
    }

    @Override
    protected boolean subEquals(AbstractEntity another) {
        Book other = (Book) another;
        return Objects.equal(edition, other.edition)
                && Objects.equal(name, other.name)
                && Objects.equal(year, other.year);
    }

    @Override
    public int compareTo(@NonNull AbstractEntity another) {
        if (!(another instanceof Book)) {
            return 1;
        } else {
            Book book = (Book) another;
            return ComparisonChain.start()
                    .compare(edition, book.edition)
                    .compare(name, book.name)
                    .compare(year, book.year)
                    .result();
        }
    }

}
