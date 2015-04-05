package com.vituel.dndplayer.model;

/**
 * Created by Victor on 04/04/2015.
 */
public interface EffectSource {

    Effect getEffect();

    void setEffect(Effect effect);

    String getName();

    void setName(String name);

    long getId();

    void setId(long id);

}
