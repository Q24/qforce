package nl.qnh.qforce.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import nl.qnh.qforce.domain.Person;
import nl.qnh.qforce.service.AnalyticService;
import nl.qnh.qforce.service.PersonServiceImplementation;


@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private AnalyticService analyticService;

    @Autowired
    private PersonServiceImplementation personService;
    
    /**
     * Endpoint for receiving a person
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get Star Wars character by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Character found",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class))),
            @ApiResponse(responseCode = "404", description = "Character not found")
    })
    public ResponseEntity<Person> getPersonById(HttpServletRequest request, @PathVariable Long id) {
        Optional<Person> person = personService.get(id);
        if (person.isPresent()) {
            analyticService.createAnalytic(request, HttpStatus.OK);
            return new ResponseEntity<>(person.get(), HttpStatus.OK);
        } else {
            analyticService.createAnalytic(request, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Endpoint for searching a person
     * @param q
     * @return
     */
    @GetMapping
    @Operation(summary = "Search for Star Wars characters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Person.class)))
    })
    public ResponseEntity<List<Person>> searchPersons(HttpServletRequest request, @RequestParam String q, @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        List<Person> personList = personService.search(q);
        analyticService.createAnalytic(request, HttpStatus.OK);
        return new ResponseEntity<>(personList, HttpStatus.OK);
    }

}
