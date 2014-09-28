package com.vituel.dndplayer.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Victor on 28/02/14.
 */
public abstract class AbstractEntity implements Serializable {

    @JsonIgnore
    protected long id;

    protected String name;

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
    public String toString() {
        return name;
    }

}
