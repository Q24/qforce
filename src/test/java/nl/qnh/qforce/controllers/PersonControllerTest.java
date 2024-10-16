package nl.qnh.qforce.controllers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PersonController.class) 
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetPersonById_Found() throws Exception {

        mockMvc.perform(get("/persons/1"))
                .andExpect(status().isOk())  
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Luke Skywalker"));
    }

    @Test
    void testGetPersonById_NotFound() throws Exception {

        mockMvc.perform(get("/persons/100"))
                .andExpect(status().isNotFound());
    }
}
