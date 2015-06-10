package com.vitobasso.d20charsheet.model;

import android.support.annotation.NonNull;

import com.vitobasso.d20charsheet.util.LangUtil;

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
        return LangUtil.hash(name);
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof AbstractEntity) {
            AbstractEntity anotherEntity = ((AbstractEntity) another);
            return ((Object) this).getClass().equals(another.getClass())
                    && name.equals(anotherEntity.name);
        }
        return false;
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
