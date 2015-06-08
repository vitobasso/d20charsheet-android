package com.vitobasso.d20charsheet.model.effect;

import com.vitobasso.d20charsheet.model.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 03/04/2015.
 */
public class Effect extends AbstractEntity {

    private String sourceName;

    private List<Modifier> modifiers = new ArrayList<>();

    public void addModifier(Modifier modifier) {
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
