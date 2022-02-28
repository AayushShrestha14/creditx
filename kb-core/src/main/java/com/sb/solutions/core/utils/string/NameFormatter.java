package com.sb.solutions.core.utils.string;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author : Rujan Maharjan on  9/5/2020
 **/
public class NameFormatter extends StdDeserializer<String> {


    public NameFormatter() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        return StringUtil.getStringWithWhiteSpaceAndWithAllFirstLetterCapitalize(
            _parseString(jsonParser, deserializationContext));
    }
}
