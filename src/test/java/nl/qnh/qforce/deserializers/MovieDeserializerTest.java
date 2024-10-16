package nl.qnh.qforce.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import nl.qnh.qforce.domain.Movie;
import nl.qnh.qforce.domain.MovieImplementation;

public class MovieDeserializerTest {

    @Test
    void testGet() {
        try {
            
            String exampleJson = "{\"title\": \"A New Hope\", \"episode_id\": 4, \"opening_crawl\": \"freedom to the galaxy....\", \"director\": \"George Lucas\", \"producer\": \"Gary Kurtz, Rick McCallum\", \"release_date\": \"1977-05-25\", \"characters\": [ \"https://swapi.dev/api/people/1/\", \"https://swapi.dev/api/people/2/\", \"https://swapi.dev/api/people/3/\", \"https://swapi.dev/api/people/4/\", \"https://swapi.dev/api/people/5/\", \"https://swapi.dev/api/people/6/\", \"https://swapi.dev/api/people/7/\", \"https://swapi.dev/api/people/8/\", \"https://swapi.dev/api/people/9/\", \"https://swapi.dev/api/people/10/\", \"https://swapi.dev/api/people/12/\", \"https://swapi.dev/api/people/13/\", \"https://swapi.dev/api/people/14/\", \"https://swapi.dev/api/people/15/\", \"https://swapi.dev/api/people/16/\", \"https://swapi.dev/api/people/18/\", \"https://swapi.dev/api/people/19/\", \"https://swapi.dev/api/people/81/\" ], \"planets\": [ \"https://swapi.dev/api/planets/1/\", \"https://swapi.dev/api/planets/2/\", \"https://swapi.dev/api/planets/3/\" ], \"starships\": [ \"https://swapi.dev/api/starships/2/\", \"https://swapi.dev/api/starships/3/\", \"https://swapi.dev/api/starships/5/\", \"https://swapi.dev/api/starships/9/\", \"https://swapi.dev/api/starships/10/\", \"https://swapi.dev/api/starships/11/\", \"https://swapi.dev/api/starships/12/\", \"https://swapi.dev/api/starships/13/\" ], \"vehicles\": [ \"https://swapi.dev/api/vehicles/4/\", \"https://swapi.dev/api/vehicles/6/\", \"https://swapi.dev/api/vehicles/7/\", \"https://swapi.dev/api/vehicles/8/\" ], \"species\": [ \"https://swapi.dev/api/species/1/\", \"https://swapi.dev/api/species/2/\", \"https://swapi.dev/api/species/3/\", \"https://swapi.dev/api/species/4/\", \"https://swapi.dev/api/species/5/\" ], \"created\": \"2014-12-10T14:23:31.880000Z\", \"edited\": \"2014-12-20T19:49:45.256000Z\", \"url\": \"https://swapi.dev/api/films/1/\"}";

            MovieImplementation expectedMovie = new MovieImplementation();
            expectedMovie.setTitle("A New Hope");
            expectedMovie.setEpisode(4);
            expectedMovie.setDirector("George Lucas");
            expectedMovie.setReleaseDate(LocalDate.of(1977, 05, 25));

            Movie actualMovie = MovieDeserializer.get(exampleJson);

            assertEquals(expectedMovie.getTitle(), actualMovie.getTitle());
            assertEquals(expectedMovie.getEpisode(), actualMovie.getEpisode());
            assertEquals(expectedMovie.getDirector(), actualMovie.getDirector());
            assertEquals(expectedMovie.getReleaseDate(), actualMovie.getReleaseDate());
            
        } catch (JsonProcessingException e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

}
