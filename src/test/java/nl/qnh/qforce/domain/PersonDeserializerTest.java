package nl.qnh.qforce.domain;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PersonDeserializerTest {

    private PersonDeserializer deserializer = new PersonDeserializer();
    private static JsonFactory jsonFactory = new ObjectMapper().getFactory();

    @Test
    void testPerson1Conversion() throws Exception {
        final PersonImpl person = getObjectFromJsonResource("person_response_id_1.json");

        assertEquals(1, person.getId());
        assertEquals(Gender.MALE, person.getGender());
        assertEquals("Luke Skywalker", person.getName());
        assertEquals(172, person.getHeight());
        assertEquals(77, person.getWeight());
        assertEquals("19BBY", person.getBirthYear());
        assertEquals(5, person.getMovieUrls().size());
    }

    @Test
    void testPerson16Conversion() throws Exception {
        final PersonImpl person = getObjectFromJsonResource("person_response_id_16.json");

        assertEquals(16, person.getId());
        assertNull(person.getGender()); // value "hermaphrodite"
        assertEquals("Jabba Desilijic Tiure", person.getName());
        assertEquals(175, person.getHeight());
        assertEquals(1358, person.getWeight());
        assertEquals("600BBY", person.getBirthYear());
        assertEquals(3, person.getMovieUrls().size());
    }

    private PersonImpl getObjectFromJsonResource(String resource) throws Exception {
        return deserializer.deserialize(jsonFactory.createParser(resourceAsString(new ClassPathResource(resource))), null);
    }

    private static String resourceAsString(Resource resource) throws Exception {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}