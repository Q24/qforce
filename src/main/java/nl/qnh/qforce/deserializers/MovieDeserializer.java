package nl.qnh.qforce.deserializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import nl.qnh.qforce.domain.Movie;
import nl.qnh.qforce.domain.MovieImplementation;

public class MovieDeserializer {
    
    /**
     * Function for deserializing a json string to a Movie object
     * @param json
     * @return
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    public static Movie get(String json) throws JsonProcessingException, JsonMappingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MovieImplementation movie = objectMapper.readValue(json, MovieImplementation.class);
        return movie;

    }
}
