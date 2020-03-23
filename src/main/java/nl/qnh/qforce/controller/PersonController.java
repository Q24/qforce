package nl.qnh.qforce.controller;

import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.service.PersonService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping(path = "persons", method = RequestMethod.GET)
@RestController
@Validated
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping
    public List<Person> searchPeople(@RequestParam("q") String query) {
        return personService.search(query);
    }

    @RequestMapping("{id}")
    public Person getPerson(@PathVariable @Min(1) long id) {
        return personService.get(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Person with id %d does not exist.", id)));
    }
}
