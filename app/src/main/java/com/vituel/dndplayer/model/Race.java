package com.vituel.dndplayer.model;

import java.util.List;

/**
 * Created by Victor on 30/03/14.
 */
public class Race extends AbstractEffect {

    private List<Trait> traits;

    public Race() {
        super(Type.RACE);
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }
}
