package com.vitobasso.d20charsheet.model.rulebook;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.vitobasso.d20charsheet.model.AbstractEntity;

/**
 * Created by Victor on 12/04/2015.
 */
public class Edition extends AbstractEntity {

    private RuleSystem system;

    public RuleSystem getSystem() {
        return system;
    }

    public void setSystem(RuleSystem system) {
        this.system = system;
    }

    @Override
    protected boolean subEquals(AbstractEntity o) {
        Edition other = (Edition) o;
        return Objects.equal(system, other.system)
                && Objects.equal(name, other.name);
    }

    @Override
    protected int subHashCode() {
        return Objects.hashCode(system, name);
    }

    @Override
    public int compareTo(@NonNull AbstractEntity another) {
        if (!(another instanceof Edition)) {
            return 1;
        } else {
            Edition edition = (Edition) another;
            return ComparisonChain.start()
                    .compare(edition.system, system)
                    .compare(name, edition.name)
                    .result();
        }
    }
}
