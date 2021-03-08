package com.library.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.api.DTOs.BookDTO;
import com.library.api.domain.Book;
import com.library.api.services.IBookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    IBookService service;

    @Test
    @DisplayName("Deve Criar um livro")
    void createBook() throws Exception {
        BookDTO dto = BookDTO.builder().id(10L).author("Jon Doe").isbn("12345").title("My book").build();
        Book savedBook = Book.builder().id(dto.getId()).author(dto.getAuthor())
                .isbn(dto.getIsbn()).title(dto.getTitle()).build();


        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(dto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()));
    }
    
    @Test
    @DisplayName("Deve Dar erro ao cadastrar um livro com dados insuficientes")
    void createBookException() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("erros", Matchers.hasSize(3)));
    }
}
