package com.vitobasso.d20charsheet.io.char_io;

import com.vitobasso.d20charsheet.model.DiceRoll;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

/**
 * Created by Victor on 10/06/2015.
 */
public class DiceRollDeserializer extends JsonDeserializer<DiceRoll> {

    @Override
    public DiceRoll deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.readValueAs(String.class);
        return new DiceRoll(str);
    }

}
