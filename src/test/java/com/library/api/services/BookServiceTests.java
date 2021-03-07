package com.library.api.services;

import com.library.api.domain.Book;
import com.library.api.repositories.BookRepository;
import com.library.api.services.impl.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTests {

    IBookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookService(repository);
    }

    @Test
    @DisplayName("Deve cadastrar um livro na base de dados")
    void createBook() {
        //cenario
        Book book = Book.builder().id(10L).author("Jon Doe").isbn("12345").title("My book").build();

        // Execução
        Mockito.when(repository.save(book)).thenReturn(Book.builder().id(book.getId())
                .author(book.getAuthor()).isbn(book.getIsbn()).title(book.getTitle()).build());

        Book savedBook = service.save(book);

        // Verificações
        Assertions.assertThat(savedBook.getId()).isNotNull().isEqualTo(10L);
        Assertions.assertThat(savedBook.getTitle()).isNotNull().isEqualTo("My book");
        Assertions.assertThat(savedBook.getAuthor()).isNotNull().isEqualTo("Jon Doe");
        Assertions.assertThat(savedBook.getIsbn()).isNotNull().isEqualTo("12345");

    }
}
