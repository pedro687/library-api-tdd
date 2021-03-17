package com.library.api.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.library.api.DTOs.BookDTO;
import com.library.api.domain.Book;
import com.library.api.exceptions.BussinesException;
import com.library.api.services.IBookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

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

    @Test
    @DisplayName("Deve dar erro ao cadastrar um livro com IBSN ja existente")
    public void createBookWithInvalidIsbn() throws Exception{
        BookDTO dto = BookDTO.builder().id(10L).author("Jon Doe").title("My book").isbn("12345").build();
        Book entity = Book.builder().id(dto.getId()).isbn(dto.getIsbn()).title(dto.getTitle())
                .author(dto.getAuthor()).build();

        BDDMockito.given(service.save(entity)).willThrow(new BussinesException("IBSN ja existente!"));

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("erros", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("erros[0]").value("IBSN ja existente!"));
    }

    @Test
    @DisplayName("Deve retornar um livro ja cadastrado por id")
    public void shouldReturnAnBookWithPassedId() throws Exception {
        Long id = 1L;
        BookDTO dto = BookDTO.builder().id(id).isbn("12345").title("My book").author("Jon Doe").build();
        Book book = Book.builder().id(dto.getId()).author(dto.getAuthor()).title(dto.getTitle())
                .isbn(dto.getIsbn()).build();

        BDDMockito.given(service.findById(id)).willReturn(Optional.of(book));
        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        //verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(dto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve retornar erro se um livro não existir")
    public void shouldReturnExceptionIfBookDoenstExist() throws Exception {
        Long id = 1L;

        BDDMockito.given(service.findById(id)).willReturn(Optional.empty());
        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        //verificação
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteAnBook() throws Exception {
    Long id = 1L;
     BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));

     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BASE_URL.concat("/" + id));

     mvc.perform(request)
             .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Deve dar erro ao deletar um livro que nao existe")
    public void deleteAnInexistentBook() throws Exception {
        Long id = 1L;
        BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(BASE_URL.concat("/" + id));

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updatingBook() throws Exception{
        BookDTO dto = BookDTO.builder().id(10L).author("Jon Doe").title("My book").isbn("12345").build();
        String json = new ObjectMapper().writeValueAsString(dto);
        Book book = Book.builder().id(10L).author(dto.getAuthor())
                .title(dto.getTitle()).isbn(dto.getIsbn())
                .build();

        BDDMockito.given(service.findById(dto.getId()))
                .willReturn(Optional.of(book));

        Book updatedBook = Book.builder().id(10L).author("Edited Autor").title("Edited Title")
                .isbn("12345").build();

        BDDMockito.given(service.update(book)).willReturn(updatedBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BASE_URL.concat("/" + dto.getId()))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(updatedBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(updatedBook.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(updatedBook.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(updatedBook.getIsbn()));
    }

    @Test
    @DisplayName("Deve dar erro ao tentar atualizar um livro inexistente")
    public void updatingInexistentBook() throws Exception {
        BookDTO dto = BookDTO.builder().id(10L).author("Jon Doe").title("My book").isbn("12345").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(BASE_URL.concat("/" + 1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve filtrar um livro")
    public void fitlerBook() throws Exception {
        Long id = 11L;

        Book book = Book.builder().id(id).isbn("12345").author("Jon Doe").title("Some book").build();

        BDDMockito.given( service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)) )
                .willReturn( new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1));

       // "/api/books?";

        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(),
                book.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BASE_URL.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
    }
}
