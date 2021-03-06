package com.library.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class BookControllerTests {
    static String BASE_URL = "/api/books";

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Deve Criar um livro")
    void createBook() throws Exception {
        String json = new ObjectMapper().writeValueAsString(null);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("Meu Livro"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("Jon Doe"));

    }
    
    @Test
    @DisplayName("Deve Dar erro ao cadastrar um livro com dados insuficientes")
    void createBookException() {

    }
}
