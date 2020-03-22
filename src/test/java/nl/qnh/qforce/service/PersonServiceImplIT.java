package nl.qnh.qforce.service;

import nl.qnh.qforce.domain.Gender;
import nl.qnh.qforce.domain.Movie;
import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.PersonImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PersonServiceImplIT {

    private static final int PERSON_ID__LUKE_SKYWALKER = 1;

    @Autowired
    private PersonService service;

    @Test
    void search() {
    }

    @Test
    void getLuke() {
        final Person person = service.get(PERSON_ID__LUKE_SKYWALKER).orElseThrow(RuntimeException::new);

        assertEquals(1, person.getId());
        assertEquals(Gender.MALE, person.getGender());
        assertEquals("Luke Skywalker", person.getName());
        assertEquals(172, person.getHeight());
        assertEquals(77, person.getWeight());
        assertEquals("19BBY", person.getBirthYear());

        final List<Movie> movies = person.getMovies();
        assertEquals(5, movies.size());

        movies.sort(Comparator.comparing(Movie::getEpisode));

        final Movie firstMovie = movies.get(0);

        assertEquals(3, firstMovie.getEpisode());
        assertEquals("Revenge of the Sith", firstMovie.getTitle());
        assertEquals("George Lucas", firstMovie.getDirector());
        assertEquals(LocalDate.of(2005, 5, 19), firstMovie.getReleaseDate());
    }

    @Test
    void unknownPersonShouldReturnEmpty() {
        assertTrue(service.get(100000L).isEmpty());
    }

    @Test
    void shouldFetchAllPersonResultsWithMovies() {

        final List<Person> people = service.search("a");
        Assertions.assertEquals(60, people.size()); // true on March 22 2020

        people.stream()
                .map(person -> (PersonImpl) person)
                .forEach(person -> {
                    Assertions.assertTrue(person.getMovies().size() > 0);
                    Assertions.assertEquals(person.getMovies().size(), person.getMovieUrls().size());
                });
    }

    @Test
    void shouldCacheAllFoundPeople() {
        final ConcurrentSkipListMap<Object, Object> cache = new ConcurrentSkipListMap<>();
        ReflectionTestUtils.setField(service, "cache", cache);

        final List<Person> people = service.search("a");
        Assertions.assertEquals(67, cache.size()); // 60 people + 7 movies
    }
}