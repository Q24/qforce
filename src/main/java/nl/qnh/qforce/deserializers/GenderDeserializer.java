package nl.qnh.qforce.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import nl.qnh.qforce.domain.Gender;

public class GenderDeserializer extends JsonDeserializer<Gender> {

    /**
     * Function for deserializing a string to a value from the Gender object
     * @param jsonParser
     * @param context
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public Gender deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        String genderString = jsonParser.getText().toLowerCase();

        switch (genderString) {
            case "male":
                return Gender.MALE;
            case "female":
                return Gender.FEMALE;
            case "n/a":
                return Gender.NOT_APPLICABLE;
            case "unknown":
                return Gender.UNKNOWN;
            default:
                throw new IOException("Invalid gender value: " + genderString);
        }
    }
    
}
