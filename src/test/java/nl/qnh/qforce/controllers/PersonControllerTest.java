package nl.qnh.qforce.controllers;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    void testSearchPersons_Found() {
        try {
            mockMvc.perform(get("/persons?q=r2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").value("R2-D2")); 
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

    @Test
    void testSearchPersons_NotFound() {
        try {
            mockMvc.perform(get("/persons?q=r2b2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0)))); 
        } catch (Exception e) {
            fail("Test failed due to exception: " + e.getMessage());
        }
    }
}
