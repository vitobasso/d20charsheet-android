package com.vitobasso.d20charsheet.model;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by Victor on 28/02/14.
 */
public abstract class AbstractEntity implements Comparable<AbstractEntity>, Serializable {

    protected long id;

    protected String name;

    protected AbstractEntity() {
    }

    protected AbstractEntity(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        if (id > 0) {
            return Objects.hashCode(id);
        } else {
            return subHashCode();
        }
    }

    protected int subHashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ((Object)this).getClass() != o.getClass()) return false;

        if (o instanceof AbstractEntity) {
            AbstractEntity another = ((AbstractEntity) o);
            if (id > 0 && another.id > 0) {
                return Objects.equal(id, another.id);
            } else {
                return subEquals(another);
            }
        }
        return false;
    }

    protected boolean subEquals(AbstractEntity another) {
        return Objects.equal(name, another.name);
    }

    @Override
    public int compareTo(@NonNull AbstractEntity another) {
        return name.compareTo(another.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
