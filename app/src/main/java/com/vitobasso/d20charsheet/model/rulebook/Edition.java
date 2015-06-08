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
    private boolean isCore;

    public RuleSystem getSystem() {
        return system;
    }

    public void setSystem(RuleSystem system) {
        this.system = system;
    }

    public boolean isCore() {
        return isCore;
    }

    public void setCore(boolean isCore) {
        this.isCore = isCore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Edition edition = (Edition) o;
        return Objects.equal(system, edition.system)
                && Objects.equal(name, edition.name);
    }

    @Override
    public int hashCode() {
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
                    .compareTrueFirst(isCore, edition.isCore)
                    .compare(name, edition.name)
                    .result();
        }
    }
}
