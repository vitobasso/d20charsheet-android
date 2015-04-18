package com.vituel.dndplayer.model.rulebook;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.vituel.dndplayer.model.AbstractEntity;

/**
 * Created by Victor on 12/04/2015.
 */
public class Book extends AbstractEntity {

    private Edition edition;
    private String abbreviation;
    private Integer year;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Book book = (Book) o;

        return Objects.equal(edition, book.edition)
                && Objects.equal(name, book.name)
                && Objects.equal(year, book.year);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(edition, name, year);
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
