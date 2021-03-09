package com.library.api.db.repositories;

import com.library.api.domain.Book;
import com.library.api.repositories.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Deve retornar true ao passar um ISBN ja existente")
    public void shouldReturnTrueIfIsbnAlreadyExist() {
        //contexto
        String isbn = "12345";
        Book book = Book.builder().id(null).isbn("12345").author("Jon Doe").title("My book").build();

        entityManager.persist(book);
        //Execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false se um isbn informado não existir na base")
    public void shouldReturnIfIsbnDosNotExist() {
        String isbn = "12345";

        boolean exists = repository.existsByIsbn(isbn);

        Assertions.assertThat(exists).isFalse();
    }
}
