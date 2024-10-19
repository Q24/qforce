package nl.qnh.qforce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.service.PersonServiceImplementation;


@RestController
public class PersonController {

    PersonServiceImplementation personService;

    public PersonController() {
        this.personService = new PersonServiceImplementation();
    }

    /**
     * Endpoint for receiving a person
     * @param id
     * @return
     */
    @GetMapping("/persons/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personService.get(id);
        if (person.isPresent()) {
            return new ResponseEntity<>(person.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Endpoint for searching a person
     * @param q
     * @return
     */
    @GetMapping("/persons")
    public ResponseEntity<List<Person>> searchPersons(@RequestParam String q) {
        List<Person> personList = personService.search(q);
        return new ResponseEntity<>(personList, HttpStatus.OK);
    }

}
