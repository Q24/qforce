package nl.qnh.qforce.deserializers;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.qnh.qforce.domain.Movie;
import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.PersonImplementation;
import nl.qnh.qforce.service.WebService;

public class PersonDeserializer {
    
    /**
     * Function for deserializing a json string to a Person object
     * @param id
     * @param json
     * @return
     * @throws JsonProcessingException
     * @throws JsonMappingException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static Person get(Long id, String json) throws JsonProcessingException, JsonMappingException, InterruptedException, ExecutionException {

        // Map the json string to PersonImplementation
        ObjectMapper objectMapper = new ObjectMapper();
        PersonImplementation person = objectMapper.readValue(json, PersonImplementation.class);

        // Set the id (since it wasn't in the response body)
        person.setId(id);
        
        
        // Get the movies if here are any
        if(person.getMovieUrls() != null) {

            // Get all the movies asynchronously
            List<HttpResponse<String>> responses = WebService.fetchMany(person.getMovieUrls());

            // Loop through the responses and a it to a list
            List<Movie> movies = new ArrayList<>();
            for (HttpResponse<String> response : responses) {

                // Deserialize movie
                Movie movie = MovieDeserializer.get(response.body());
                movies.add(movie);
                            
            }
            person.setMovies(movies);
        }
        
        // Return the person object
        return person;

    }
 
}
