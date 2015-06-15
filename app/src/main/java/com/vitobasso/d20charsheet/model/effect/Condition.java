package com.vitobasso.d20charsheet.model.effect;

import com.google.common.base.Objects;
import com.vitobasso.d20charsheet.model.AbstractEntity;

import org.codehaus.jackson.annotate.JsonValue;

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

    private Condition parent;

    public Condition() {
    }

    //used by jackson
    public Condition(long id) {
        this.id = id;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public Condition getParent() {
        return parent;
    }

    public void setParent(Condition parent) {
        this.parent = parent;
    }

    @JsonValue
    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    protected int subHashCode() {
        return Objects.hashCode(predicate, name);
    }

    @Override
    protected boolean subEquals(AbstractEntity another) {
        Condition other = (Condition) another;
        return Objects.equal(predicate, other.predicate)
                && Objects.equal(name, other.name);
    }

    @Override
    public String toString() {
        return String.format("%s %s", getPredicate(),name);
    }

}
