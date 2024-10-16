package nl.qnh.qforce.deserializers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import nl.qnh.qforce.domain.Gender;
import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.domain.PersonImplementation;

public class PersonDeserializerTest {
    @Test
    void testGet() {
        try {
            
            String exampleJson = "{\"name\": \"Luke Skywalker\", \"height\": \"172\", \"mass\": \"77\", \"hair_color\": \"blond\", \"skin_color\": \"fair\", \"eye_color\": \"blue\", \"birth_year\": \"19BBY\", \"gender\": \"male\", \"homeworld\": \"https://swapi.dev/api/planets/1/\", \"species\": [], \"vehicles\": [   \"https://swapi.dev/api/vehicles/14/\",   \"https://swapi.dev/api/vehicles/30/\" ], \"starships\": [   \"https://swapi.dev/api/starships/12/\",   \"https://swapi.dev/api/starships/22/\" ], \"created\": \"2014-12-09T13:50:51.644000Z\", \"edited\": \"2014-12-20T21:17:56.891000Z\", \"url\": \"https://swapi.dev/api/people/1/\"}";

            PersonImplementation expectedPerson = new PersonImplementation();
            expectedPerson.setId(1);
            expectedPerson.setName("Luke Skywalker");
            expectedPerson.setHeight(172);
            expectedPerson.setWeight(77);
            expectedPerson.setGender(Gender.MALE);
            expectedPerson.setBirthYear("19BBY");


            Person actualPerson = PersonDeserializer.get(1, exampleJson);

            assertEquals(expectedPerson.getId(), actualPerson.getId());
            assertEquals(expectedPerson.getName(), actualPerson.getName());
            assertEquals(expectedPerson.getHeight(), actualPerson.getHeight());
            assertEquals(expectedPerson.getWeight(), actualPerson.getWeight());
            assertEquals(expectedPerson.getGender(), actualPerson.getGender());
            assertEquals(expectedPerson.getBirthYear(), actualPerson.getBirthYear());
            
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }
}
