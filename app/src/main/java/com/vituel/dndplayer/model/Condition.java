package com.vituel.dndplayer.model;

import java.security.InvalidParameterException;

/**
 * Created by Victor on 22/04/14.
 */
public class Condition extends AbstractEntity {

    public enum Predicate {
        WHEN, USING, AGAINST, RELATED_TO;

        public static Predicate fromString(String str) {
            if (str.equalsIgnoreCase("when")) {
                return WHEN;
            } else if (str.equalsIgnoreCase("using")) {
                return USING;
            } else if (str.equalsIgnoreCase("against")) {
                return AGAINST;
            } else if (str.equalsIgnoreCase("related to")) {
                return RELATED_TO;
            } else {
                throw new InvalidParameterException();
            }
        }
    }

    private Predicate predicate;

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Condition) {
            Condition other = (Condition) o;
            return getPredicate() == other.getPredicate() && name.equals(other.name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", getPredicate(),name);
    }
}
