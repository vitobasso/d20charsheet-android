package com.vitobasso.d20charsheet.util.business.exception;

/**
 * Created by Victor on 27/06/2015.
 */
public class EffectApplicationException extends RuntimeException {

    public EffectApplicationException() {
    }

    public EffectApplicationException(String detailMessage) {
        super(detailMessage);
    }
}
