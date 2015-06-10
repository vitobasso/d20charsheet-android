package com.vitobasso.d20charsheet.io.char_io;

import com.vitobasso.d20charsheet.model.DiceRoll;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Created by Victor on 10/06/2015.
 */
public class DiceRollSerializer extends JsonSerializer<DiceRoll> {

    @Override
    public void serialize(DiceRoll value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(value.toString());
    }

}
