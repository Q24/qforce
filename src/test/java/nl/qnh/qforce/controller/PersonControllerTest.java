package nl.qnh.qforce.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void personIdShouldBePositiveInteger() {
        final Exception exception = Assertions.assertThrows(NestedServletException.class,
                () -> mockMvc.perform(MockMvcRequestBuilders.get("/persons/{id}", 0L)));

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains("getPerson.id: must be greater than or equal to 1"));
    }
}