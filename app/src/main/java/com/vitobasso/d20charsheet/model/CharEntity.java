package com.vitobasso.d20charsheet.model;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Entity tied to a character, as opposed to rules (which exist independently)
 * This is currently a marker interface to help configure jackson (to ignore the id when serializing these entities)
 * Created by Victor on 10/06/2015.
 */
public interface CharEntity {

    @JsonIgnore
    long getId();

}
