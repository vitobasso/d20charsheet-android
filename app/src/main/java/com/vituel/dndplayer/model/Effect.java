package com.vituel.dndplayer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 03/04/2015.
 */
public class Effect extends AbstractEntity {

    private String sourceName;

    private List<Modifier> modifiers;

    public void addModifier(Modifier modifier) {
        if (modifiers == null) {
            modifiers = new ArrayList<>();
        }
        modifiers.add(modifier);
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
