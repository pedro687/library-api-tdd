package com.library.api.services;

import com.library.api.domain.Book;
import com.library.api.exceptions.BussinesException;
import com.library.api.repositories.BookRepository;
import com.library.api.services.impl.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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

    @Test
    @DisplayName("Não deve cadastrar produto com o mesmo ISBN")
    public void shouldNotBeCreateWithDuplicatedISBN() {
        Book book = Book.builder().id(10L).author("Jon Doe").isbn("12345").title("My book").build();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        assertThat(exception).isInstanceOf(BussinesException.class)
                .hasMessage("IBSN ja existente!");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Busca por um livro passado por ID na base de dados")
    public void findBook() {
        Long id = 10L;
        Book book = Book.builder().id(id).isbn("12345").title("My book").author("Jon Doe").build();

        Mockito.when( service.findById(id) ).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.findById(id);

        Assertions.assertThat(foundBook.get().getId()).isNotNull().isEqualTo(book.getId());
        Assertions.assertThat(foundBook.get().getIsbn()).isNotNull().isEqualTo(book.getIsbn());
        Assertions.assertThat(foundBook.get().getAuthor()).isNotNull().isEqualTo(book.getAuthor());
        Assertions.assertThat(foundBook.get().getTitle()).isNotNull().isEqualTo(book.getTitle());
    }

    @Test
    @DisplayName("Retorna erro se o livro nao existir")
    public void notFoundBook() {
        Long id = 10L;

        BDDMockito.given(service.findById(id)).willReturn(Optional.empty());

        Optional<Book> notFoundBook = service.findById(id);

        Assertions.assertThat(notFoundBook.isPresent()).isFalse();
    }

}

